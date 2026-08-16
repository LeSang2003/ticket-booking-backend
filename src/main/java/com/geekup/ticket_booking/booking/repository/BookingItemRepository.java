package com.geekup.ticket_booking.booking.repository;

import com.geekup.ticket_booking.booking.entity.BookingItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingItemRepository extends JpaRepository<BookingItem, Long> {
    List<BookingItem> findByBooking_Id(Long bookingId);
}