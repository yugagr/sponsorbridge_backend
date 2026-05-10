package com.sponsorbridge.sponsorbridge.dto;

import com.sponsorbridge.sponsorbridge.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;       // JWT token
    private String name;
    private String email;
    private Role role;
    private Long userId;
}