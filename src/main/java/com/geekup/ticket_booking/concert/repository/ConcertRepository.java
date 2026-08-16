package com.geekup.ticket_booking.concert.repository;

import com.geekup.ticket_booking.concert.entity.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ConcertRepository extends JpaRepository<Concert, Long> {

    @Modifying
    @Query("UPDATE Concert c SET c.availableSeats = c.availableSeats - :qty " +
           "WHERE c.id = :id AND c.availableSeats >= :qty")
    int tryReserveSeats(@Param("id") Long id, @Param("qty") int qty);
}