package com.geekup.ticket_booking.tickettype.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TicketTypeResponse {

    private Long id;

    private String name;

    private Double price;

    private Integer quantity;

    private Long concertId;
}