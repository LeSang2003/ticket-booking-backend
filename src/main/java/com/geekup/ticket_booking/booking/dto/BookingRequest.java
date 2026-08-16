package com.geekup.ticket_booking.booking.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class BookingRequest {

    @NotNull
    private Long concertId;

    @NotEmpty
    @Valid
    private List<BookingItemRequest> items;

    @NotBlank
    private String idempotencyKey;

    private String voucherCode; // optional
}