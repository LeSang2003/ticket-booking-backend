package com.geekup.ticket_booking.booking.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingItemView {
    private String ticketTypeName;
    private Integer quantity;
    private Double unitPrice;
}