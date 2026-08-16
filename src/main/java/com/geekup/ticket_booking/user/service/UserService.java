package com.geekup.ticket_booking.user.service;

import com.geekup.ticket_booking.user.dto.RegisterRequest;
import com.geekup.ticket_booking.user.dto.LoginRequest;
import com.geekup.ticket_booking.user.dto.LoginResponse;
public interface UserService {

    void register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

}