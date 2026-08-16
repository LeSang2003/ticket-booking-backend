package com.geekup.ticket_booking.booking.entity;

import com.geekup.ticket_booking.common.entity.BaseEntity;
import com.geekup.ticket_booking.concert.entity.Concert;
import com.geekup.ticket_booking.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bookings",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "idempotency_key"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "concert_id")
    private Concert concert;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @Column(name = "idempotency_key", nullable = false)
    private String idempotencyKey;

    @Column(nullable = false)
    private Double subtotalAmount;

    @Column(nullable = false)
    private Double discountAmount;

    @Column(nullable = false)
    private Double totalAmount;
}