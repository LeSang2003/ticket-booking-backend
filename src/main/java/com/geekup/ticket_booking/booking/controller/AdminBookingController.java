package com.geekup.ticket_booking.booking.controller;

import com.geekup.ticket_booking.booking.dto.AdminBookingResponse;
import com.geekup.ticket_booking.booking.dto.DashboardResponse;
import com.geekup.ticket_booking.booking.dto.UpdateBookingStatusRequest;
import com.geekup.ticket_booking.booking.entity.BookingStatus;
import com.geekup.ticket_booking.booking.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/bookings")
@RequiredArgsConstructor
public class AdminBookingController {

    private final BookingService bookingService;

    @GetMapping
    public List<AdminBookingResponse> getAll(@RequestParam(required = false) BookingStatus status) {
        return bookingService.getAllForAdmin(status);
    }

    @PutMapping("/{id}/status")
    public void updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateBookingStatusRequest request) {
        bookingService.updateStatus(id, request.getStatus());
    }

    @GetMapping("/dashboard")
    public DashboardResponse dashboard() {
        return bookingService.getDashboard();
    }
}