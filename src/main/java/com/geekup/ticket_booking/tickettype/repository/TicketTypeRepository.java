package com.geekup.ticket_booking.tickettype.repository;

import com.geekup.ticket_booking.tickettype.entity.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketTypeRepository extends JpaRepository<TicketType, Long> {

    List<TicketType> findByConcertId(Long concertId);

    @Modifying
    @Query("UPDATE TicketType t SET t.quantity = t.quantity - :qty " +
           "WHERE t.id = :id AND t.quantity >= :qty")
    int tryReserve(@Param("id") Long id, @Param("qty") int qty);
}