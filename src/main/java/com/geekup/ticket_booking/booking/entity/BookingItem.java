package com.geekup.ticket_booking.booking.entity;

import com.geekup.ticket_booking.common.entity.BaseEntity;
import com.geekup.ticket_booking.tickettype.entity.TicketType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "booking_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingItem extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "ticket_type_id")
    private TicketType ticketType;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Double unitPrice; // snapshot giá lúc đặt, không lấy giá hiện tại của TicketType
}