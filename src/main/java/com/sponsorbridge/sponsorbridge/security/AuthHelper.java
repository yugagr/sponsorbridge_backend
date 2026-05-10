package com.sponsorbridge.sponsorbridge.security;

import com.sponsorbridge.sponsorbridge.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthHelper {

    // call this from any service to get the currently logged-in user
    public User getCurrentUser() {
        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }
}