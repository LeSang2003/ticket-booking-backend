package com.geekup.ticket_booking.voucher.dto;

import com.geekup.ticket_booking.voucher.entity.DiscountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VoucherRequest {
    @NotBlank
    private String code;

    @NotNull
    private DiscountType discountType;

    @NotNull
    @Positive
    private Double discountValue;

    @NotNull
    @Positive
    private Integer totalQuantity;

    @NotNull
    private LocalDateTime validFrom;

    @NotNull
    private LocalDateTime validTo;
}