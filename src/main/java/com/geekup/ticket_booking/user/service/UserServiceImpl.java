package com.geekup.ticket_booking.user.service;

import com.geekup.ticket_booking.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.geekup.ticket_booking.user.dto.RegisterRequest;
import com.geekup.ticket_booking.user.entity.Role;
import com.geekup.ticket_booking.user.entity.User;
import com.geekup.ticket_booking.security.JwtService;
import com.geekup.ticket_booking.user.dto.LoginRequest;
import com.geekup.ticket_booking.user.dto.LoginResponse;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    @Override
    public void register(RegisterRequest request) {

      if (userRepository.existsByEmail(request.getEmail())) {
        throw new RuntimeException("Email already exists");
      }

      if (userRepository.existsByPhone(request.getPhone())) {
        throw new RuntimeException("Phone already exists");
      }

      User user = User.builder()
            .fullName(request.getFullName())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .phone(request.getPhone())
            .role(Role.CUSTOMER)
            .build();

        userRepository.save(user);
    }

    @Override
public LoginResponse login(LoginRequest request) {

    User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("Invalid email or password"));

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
        throw new RuntimeException("Invalid email or password");
    }

    String token = jwtService.generateToken(user.getEmail(), user.getRole().name());

    return new LoginResponse(token);
}

}