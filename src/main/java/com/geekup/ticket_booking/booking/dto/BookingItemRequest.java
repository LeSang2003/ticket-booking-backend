package com.geekup.ticket_booking.booking.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class BookingItemRequest {

    @NotNull
    private Long ticketTypeId;

    @NotNull
    @Positive
    private Integer quantity;
}