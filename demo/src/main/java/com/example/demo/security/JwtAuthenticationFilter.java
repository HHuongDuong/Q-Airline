package com.example.demo.security;

import com.example.demo.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, @Lazy UserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        String jwt = null;
        String username = null;

        System.out.println("Request URI: " + request.getRequestURI());

        // Try to get JWT from Authorization header
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            System.out.println("JWT found in Authorization header.");
        } else if (request.getCookies() != null) {
            // If not in header, try to get JWT from cookies
            System.out.println("Checking cookies for JWT.");
            for (Cookie cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    jwt = cookie.getValue();
                    System.out.println("JWT found in cookie: " + jwt);
                    break;
                }
            }
        } else {
            System.out.println("No Authorization header and no cookies found.");
        }

        if (jwt == null) {
            System.out.println("No JWT found. Continuing filter chain.");
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("Attempting to get username from JWT: " + jwt);
        username = jwtTokenProvider.getUsernameFromToken(jwt);
        System.out.println("Username from JWT: " + username);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userService.findByUsername(username)
                    .orElse(null);

            if (userDetails == null) {
                System.out.println("User details not found for username: " + username);
            } else {
                System.out.println("User details found for: " + username);
            }

            if (userDetails != null && jwtTokenProvider.validateToken(jwt)) {
                System.out.println("JWT valid. Setting authentication context for: " + username);
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else if (userDetails != null) {
                System.out.println("JWT invalid for user: " + username);
            }
        }
        filterChain.doFilter(request, response);
    }
} 