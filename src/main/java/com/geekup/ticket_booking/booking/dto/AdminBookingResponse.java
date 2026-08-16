package com.geekup.ticket_booking.booking.dto;

import com.geekup.ticket_booking.booking.entity.BookingStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class AdminBookingResponse {
    private Long id;
    private String userEmail;
    private String concertName;
    private List<BookingItemView> items;
    private Double totalAmount;
    private BookingStatus status;
    private LocalDateTime createdAt;
}