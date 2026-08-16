package com.geekup.ticket_booking.booking.service.impl;

import com.geekup.ticket_booking.booking.dto.*;
import com.geekup.ticket_booking.booking.entity.Booking;
import com.geekup.ticket_booking.booking.entity.BookingItem;
import com.geekup.ticket_booking.booking.entity.BookingStatus;
import com.geekup.ticket_booking.booking.repository.BookingItemRepository;
import com.geekup.ticket_booking.booking.repository.BookingRepository;
import com.geekup.ticket_booking.booking.service.BookingService;
import com.geekup.ticket_booking.concert.entity.Concert;
import com.geekup.ticket_booking.concert.repository.ConcertRepository;
import com.geekup.ticket_booking.tickettype.entity.TicketType;
import com.geekup.ticket_booking.tickettype.repository.TicketTypeRepository;
import com.geekup.ticket_booking.user.entity.User;
import com.geekup.ticket_booking.user.repository.UserRepository;
import com.geekup.ticket_booking.voucher.entity.DiscountType;
import com.geekup.ticket_booking.voucher.entity.Voucher;
import com.geekup.ticket_booking.voucher.entity.VoucherUsage;
import com.geekup.ticket_booking.voucher.repository.VoucherRepository;
import com.geekup.ticket_booking.voucher.repository.VoucherUsageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingItemRepository bookingItemRepository;
    private final ConcertRepository concertRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final UserRepository userRepository;
    private final VoucherRepository voucherRepository;
    private final VoucherUsageRepository voucherUsageRepository;

    @Override
    @Transactional
    public BookingResponse create(BookingRequest request, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // idempotency: đã đặt với key này rồi -> trả booking cũ, không tạo mới
        var existing = bookingRepository.findByUserAndIdempotencyKey(user, request.getIdempotencyKey());
        if (existing.isPresent()) {
            return toResponse(existing.get());
        }

        Concert concert = concertRepository.findById(request.getConcertId())
                .orElseThrow(() -> new RuntimeException("Concert not found"));

        // --- Bước 1: tạo booking rỗng trước, để có ID gắn cho từng item ---
        Booking booking = Booking.builder()
                .user(user)
                .concert(concert)
                .status(BookingStatus.PENDING_PAYMENT)
                .idempotencyKey(request.getIdempotencyKey())
                .subtotalAmount(0.0)
                .discountAmount(0.0)
                .totalAmount(0.0)
                .build();
        booking = bookingRepository.save(booking);

        // --- Bước 2: xử lý từng item trong giỏ hàng (cart) ---
        double subtotal = 0.0;
        int totalTicketCount = 0;
        List<BookingItem> savedItems = new ArrayList<>();

        for (BookingItemRequest itemReq : request.getItems()) {
            TicketType ticketType = ticketTypeRepository.findById(itemReq.getTicketTypeId())
                    .orElseThrow(() -> new RuntimeException("Ticket type not found: " + itemReq.getTicketTypeId()));

            // atomic reserve — đúng cơ chế chống oversell đã học
            int updated = ticketTypeRepository.tryReserve(ticketType.getId(), itemReq.getQuantity());
            if (updated == 0) {
                // throw ở đây -> @Transactional rollback TOÀN BỘ, kể cả các item
                // đã tryReserve thành công trước đó trong vòng lặp này
                throw new RuntimeException("Not enough tickets for: " + ticketType.getName());
            }

            BookingItem item = BookingItem.builder()
                    .booking(booking)
                    .ticketType(ticketType)
                    .quantity(itemReq.getQuantity())
                    .unitPrice(ticketType.getPrice())
                    .build();
            savedItems.add(bookingItemRepository.save(item));

            subtotal += ticketType.getPrice() * itemReq.getQuantity();
            totalTicketCount += itemReq.getQuantity();
        }

        // --- Bước 3: trừ ghế concert theo TỔNG số vé của cả giỏ hàng ---
        int seatUpdated = concertRepository.tryReserveSeats(concert.getId(), totalTicketCount);
        if (seatUpdated == 0) {
            throw new RuntimeException("Concert is full");
        }

        // --- Bước 4: áp voucher (nếu có) trên tổng subtotal ---
        double discount = 0.0;
        if (request.getVoucherCode() != null && !request.getVoucherCode().isBlank()) {
            Voucher voucher = voucherRepository.findByCode(request.getVoucherCode())
                    .orElseThrow(() -> new RuntimeException("Voucher not found"));

            if (LocalDateTime.now().isAfter(voucher.getValidTo())) {
                throw new RuntimeException("Voucher has expired");
            }
            if (voucherUsageRepository.existsByVoucherAndUser(voucher, user)) {
                throw new RuntimeException("You have already used this voucher");
            }
            int voucherUpdated = voucherRepository.tryDecrement(voucher.getId());
            if (voucherUpdated == 0) {
                throw new RuntimeException("Voucher has been fully redeemed");
            }

            discount = voucher.getDiscountType() == DiscountType.PERCENTAGE
                    ? subtotal * voucher.getDiscountValue() / 100
                    : Math.min(voucher.getDiscountValue(), subtotal);

            voucherUsageRepository.save(VoucherUsage.builder()
                    .voucher(voucher).user(user).booking(booking).build());
        }

        // --- Bước 5: cập nhật lại tổng tiền cho booking ---
        booking.setSubtotalAmount(subtotal);
        booking.setDiscountAmount(discount);
        booking.setTotalAmount(subtotal - discount);
        booking = bookingRepository.save(booking);

        return toResponse(booking);
    }

    /** Mock payment: giả lập thanh toán thành công, chuyển PENDING_PAYMENT -> CONFIRMED. */
    @Override
    @Transactional
    public BookingResponse pay(Long bookingId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("This booking does not belong to you");
        }
        if (booking.getStatus() != BookingStatus.PENDING_PAYMENT) {
            throw new RuntimeException("Only PENDING_PAYMENT bookings can be paid");
        }

        booking.setStatus(BookingStatus.CONFIRMED);
        booking = bookingRepository.save(booking);
        return toResponse(booking);
    }

    @Override
    public List<BookingHistoryResponse> getMyBookings(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return bookingRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .map(b -> BookingHistoryResponse.builder()
                        .id(b.getId())
                        .concertName(b.getConcert().getTitle())
                        .items(toItemViews(b.getId()))
                        .totalAmount(b.getTotalAmount())
                        .status(b.getStatus())
                        .createdAt(b.getCreatedAt())
                        .build())
                .toList();
    }

    @Override
    @Transactional
    public void cancel(Long bookingId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("This booking does not belong to you");
        }
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new RuntimeException("Booking already cancelled");
        }

        releaseInventory(booking);
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }

    @Override
    public List<AdminBookingResponse> getAllForAdmin(BookingStatus status) {
        List<Booking> bookings = (status == null)
                ? bookingRepository.findAll()
                : bookingRepository.findByStatus(status);

        return bookings.stream()
                .map(b -> AdminBookingResponse.builder()
                        .id(b.getId())
                        .userEmail(b.getUser().getEmail())
                        .concertName(b.getConcert().getTitle())
                        .items(toItemViews(b.getId()))
                        .totalAmount(b.getTotalAmount())
                        .status(b.getStatus())
                        .createdAt(b.getCreatedAt())
                        .build())
                .toList();
    }

    @Override
    @Transactional
    public void updateStatus(Long bookingId, BookingStatus newStatus) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        boolean releasingInventory = booking.getStatus() != BookingStatus.CANCELLED
                && newStatus == BookingStatus.CANCELLED;
        if (releasingInventory) {
            releaseInventory(booking);
        }

        booking.setStatus(newStatus);
        bookingRepository.save(booking);
    }

    @Override
    public DashboardResponse getDashboard() {
        List<Booking> all = bookingRepository.findAll();

        long confirmed = all.stream().filter(b -> b.getStatus() == BookingStatus.CONFIRMED).count();
        long cancelled = all.stream().filter(b -> b.getStatus() == BookingStatus.CANCELLED).count();
        double revenue = all.stream()
                .filter(b -> b.getStatus() == BookingStatus.CONFIRMED)
                .mapToDouble(Booking::getTotalAmount).sum();

        long ticketsSold = 0;
        for (Booking b : all) {
            if (b.getStatus() == BookingStatus.CONFIRMED) {
                ticketsSold += bookingItemRepository.findByBooking_Id(b.getId()).stream()
                        .mapToInt(BookingItem::getQuantity).sum();
            }
        }

        return DashboardResponse.builder()
                .totalConcerts(concertRepository.count())
                .totalBookings(all.size())
                .confirmedBookings(confirmed)
                .cancelledBookings(cancelled)
                .totalRevenue(revenue)
                .totalTicketsSold(ticketsSold)
                .build();
    }

    // --- helpers ---

    /** Hoàn lại vé + ghế cho TẤT CẢ item của 1 booking (dùng khi cancel). */
    private void releaseInventory(Booking booking) {
        List<BookingItem> items = bookingItemRepository.findByBooking_Id(booking.getId());
        int totalQty = 0;
        for (BookingItem item : items) {
            TicketType tt = item.getTicketType();
            tt.setQuantity(tt.getQuantity() + item.getQuantity());
            ticketTypeRepository.save(tt);
            totalQty += item.getQuantity();
        }
        Concert concert = booking.getConcert();
        concert.setAvailableSeats(concert.getAvailableSeats() + totalQty);
        concertRepository.save(concert);
    }

    private List<BookingItemView> toItemViews(Long bookingId) {
        return bookingItemRepository.findByBooking_Id(bookingId).stream()
                .map(i -> BookingItemView.builder()
                        .ticketTypeName(i.getTicketType().getName())
                        .quantity(i.getQuantity())
                        .unitPrice(i.getUnitPrice())
                        .build())
                .toList();
    }

    private BookingResponse toResponse(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .concertName(booking.getConcert().getTitle())
                .items(toItemViews(booking.getId()))
                .subtotalAmount(booking.getSubtotalAmount())
                .discountAmount(booking.getDiscountAmount())
                .totalAmount(booking.getTotalAmount())
                .status(booking.getStatus())
                .build();
    }
}