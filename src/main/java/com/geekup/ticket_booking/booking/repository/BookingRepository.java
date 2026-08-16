package com.geekup.ticket_booking.booking.repository;

import com.geekup.ticket_booking.booking.entity.Booking;
import com.geekup.ticket_booking.booking.entity.BookingStatus;
import com.geekup.ticket_booking.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByUserAndIdempotencyKey(User user, String idempotencyKey);

    List<Booking> findByUserOrderByCreatedAtDesc(User user);

    List<Booking> findByStatus(BookingStatus status);
}