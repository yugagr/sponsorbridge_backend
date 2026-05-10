package com.sponsorbridge.sponsorbridge.security;

import com.sponsorbridge.sponsorbridge.entity.User;
import com.sponsorbridge.sponsorbridge.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    // OncePerRequestFilter = runs exactly once per request

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. get the Authorization header
        String authHeader = request.getHeader("Authorization");

        // 2. check if header exists and starts with "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);  // no token — move on
            return;
        }

        // 3. extract the token (remove "Bearer " prefix)
        String token = authHeader.substring(7);

        // 4. validate token
        if (!jwtUtil.isTokenValid(token)) {
            filterChain.doFilter(request, response);  // invalid token — move on
            return;
        }

        // 5. extract email from token
        String email = jwtUtil.extractEmail(token);

        // 6. load user from DB
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // 7. set user as authenticated in Spring Security context
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        user,       // principal — the logged in user object
                        null,       // credentials — not needed after authentication
                        List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 8. continue to the controller
        filterChain.doFilter(request, response);
    }
}