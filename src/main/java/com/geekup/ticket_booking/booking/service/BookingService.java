package com.geekup.ticket_booking.booking.service;

import com.geekup.ticket_booking.booking.dto.*;
import com.geekup.ticket_booking.booking.entity.BookingStatus;

import java.util.List;

public interface BookingService {
    BookingResponse create(BookingRequest request, String email);
    BookingResponse pay(Long bookingId, String email);
    List<BookingHistoryResponse> getMyBookings(String email);
    void cancel(Long bookingId, String email);
    List<AdminBookingResponse> getAllForAdmin(BookingStatus status);
    void updateStatus(Long bookingId, BookingStatus newStatus);
    DashboardResponse getDashboard();
}