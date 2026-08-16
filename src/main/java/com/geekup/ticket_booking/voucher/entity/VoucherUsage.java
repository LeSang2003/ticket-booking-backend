package com.geekup.ticket_booking.voucher.entity;

import com.geekup.ticket_booking.booking.entity.Booking;
import com.geekup.ticket_booking.common.entity.BaseEntity;
import com.geekup.ticket_booking.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "voucher_usages",
        uniqueConstraints = @UniqueConstraint(columnNames = {"voucher_id", "user_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoucherUsage extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "voucher_id")
    private Voucher voucher;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;
}