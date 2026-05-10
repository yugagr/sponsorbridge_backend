package com.sponsorbridge.sponsorbridge.service;

import com.sponsorbridge.sponsorbridge.dto.AuthResponse;
import com.sponsorbridge.sponsorbridge.dto.LoginRequest;
import com.sponsorbridge.sponsorbridge.dto.RegisterRequest;
import com.sponsorbridge.sponsorbridge.entity.User;
import com.sponsorbridge.sponsorbridge.repository.UserRepository;
import com.sponsorbridge.sponsorbridge.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;          // added

    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        User savedUser = userRepository.save(user);

        // generate real token now
        String token = jwtUtil.generateToken(savedUser.getEmail());

        return new AuthResponse(
                token,              // real token instead of null
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole(),
                savedUser.getId()
        );
    }

    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        // generate real token now
        String token = jwtUtil.generateToken(user.getEmail());

        return new AuthResponse(
                token,              // real token instead of null
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getId()
        );
    }
}