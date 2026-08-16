package com.geekup.ticket_booking.booking.controller;

import com.geekup.ticket_booking.booking.dto.BookingHistoryResponse;
import com.geekup.ticket_booking.booking.dto.BookingRequest;
import com.geekup.ticket_booking.booking.dto.BookingResponse;
import com.geekup.ticket_booking.booking.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingResponse create(@Valid @RequestBody BookingRequest request,
                                   Authentication authentication) {
        return bookingService.create(request, authentication.getName());
    }

    /** Mock payment endpoint — giả lập thanh toán thành công. */
    @PostMapping("/{id}/pay")
    public BookingResponse pay(@PathVariable Long id, Authentication authentication) {
        return bookingService.pay(id, authentication.getName());
    }

    @GetMapping("/my")
    public List<BookingHistoryResponse> getMyBookings(Authentication authentication) {
        return bookingService.getMyBookings(authentication.getName());
    }

    @PatchMapping("/{id}/cancel")
    public void cancel(@PathVariable Long id, Authentication authentication) {
        bookingService.cancel(id, authentication.getName());
    }
}