package com.geekup.ticket_booking.voucher.repository;

import com.geekup.ticket_booking.voucher.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VoucherRepository extends JpaRepository<Voucher, Long> {

    Optional<Voucher> findByCode(String code);

    @Modifying
    @Query("UPDATE Voucher v SET v.remainingQuantity = v.remainingQuantity - 1 " +
           "WHERE v.id = :id AND v.remainingQuantity >= 1")
    int tryDecrement(@Param("id") Long id);
}