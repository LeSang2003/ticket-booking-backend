package com.geekup.ticket_booking.booking.entity;

public enum BookingStatus {
    PENDING_PAYMENT,  // vừa tạo, đã giữ vé, chờ thanh toán
    CONFIRMED,        // đã thanh toán (mock)
    CANCELLED
}