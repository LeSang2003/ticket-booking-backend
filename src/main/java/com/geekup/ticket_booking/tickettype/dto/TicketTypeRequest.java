package com.geekup.ticket_booking.tickettype.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class TicketTypeRequest {

    @NotBlank
    private String name;

    @NotNull
    @PositiveOrZero
    private Double price;

    @NotNull
    @Positive
    private Integer quantity;

    @NotNull
    private Long concertId;
}