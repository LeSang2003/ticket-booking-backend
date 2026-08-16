package com.geekup.ticket_booking.voucher.dto;

import com.geekup.ticket_booking.voucher.entity.DiscountType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class VoucherResponse {
    private Long id;
    private String code;
    private DiscountType discountType;
    private Double discountValue;
    private Integer totalQuantity;
    private Integer remainingQuantity;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;
}