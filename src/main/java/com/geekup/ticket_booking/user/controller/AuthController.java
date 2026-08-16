package com.geekup.ticket_booking.user.controller;

import com.geekup.ticket_booking.user.dto.RegisterRequest;
import com.geekup.ticket_booking.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.geekup.ticket_booking.user.dto.LoginRequest;
import com.geekup.ticket_booking.user.dto.LoginResponse;
import jakarta.validation.Valid;
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public String register(@Valid @RequestBody RegisterRequest request){

        userService.register(request);

        return "Register successfully";
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request){

      return userService.login(request);
    }

}