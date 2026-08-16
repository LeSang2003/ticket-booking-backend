package com.geekup.ticket_booking.booking.dto;

import com.geekup.ticket_booking.booking.entity.BookingStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateBookingStatusRequest {
    @NotNull
    private BookingStatus status;
}