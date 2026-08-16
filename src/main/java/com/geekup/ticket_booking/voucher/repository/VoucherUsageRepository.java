package com.geekup.ticket_booking.voucher.repository;

import com.geekup.ticket_booking.voucher.entity.Voucher;
import com.geekup.ticket_booking.voucher.entity.VoucherUsage;
import com.geekup.ticket_booking.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoucherUsageRepository extends JpaRepository<VoucherUsage, Long> {
    boolean existsByVoucherAndUser(Voucher voucher, User user);
}