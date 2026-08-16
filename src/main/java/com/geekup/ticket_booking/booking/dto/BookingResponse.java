package com.geekup.ticket_booking.booking.dto;

import com.geekup.ticket_booking.booking.entity.BookingStatus;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BookingResponse {
    private Long id;
    private String concertName;
    private List<BookingItemView> items;
    private Double subtotalAmount;
    private Double discountAmount;
    private Double totalAmount;
    private BookingStatus status;
}