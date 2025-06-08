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
            String potentialJwt = authHeader.substring(7);
            // Only use JWT from header if it's not empty and not the literal string "undefined"
            if (!potentialJwt.isEmpty() && !potentialJwt.equals("undefined")) {
                jwt = potentialJwt;
                System.out.println("JWT found in Authorization header.");
            } else {
                System.out.println("Authorization header found but JWT part is empty or undefined.");
            }
        }

        // If JWT is still null (not found in header or header was empty/malformed/undefined), try to get from cookies
        if (jwt == null && request.getCookies() != null) {
            System.out.println("Checking cookies for JWT.");
            for (Cookie cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    jwt = cookie.getValue();
                    System.out.println("JWT found in cookie: " + jwt);
                    break;
                }
            }
        }

        if (jwt == null) {
            System.out.println("No JWT found. Continuing filter chain.");
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("Attempting to get username from JWT: " + jwt);
        try {
            username = jwtTokenProvider.getUsernameFromToken(jwt);
            System.out.println("Username from JWT: " + username);
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            System.out.println("Malformed JWT: " + e.getMessage());
            // Clear the invalid JWT to prevent further processing with it
            jwt = null; // Set jwt to null so subsequent authentication check fails with this malformed token
            // If the JWT is malformed, we can stop processing this token and let the filter chain continue
            // This will ensure that if the token is malformed, the request is treated as unauthenticated.
            // If the request is for a secured endpoint, the exceptionHandling will take over.
            filterChain.doFilter(request, response);
            return; // Important: Exit here after handling malformed JWT
        } catch (Exception e) { // Catch any other unexpected exceptions during token parsing
            System.out.println("Error processing JWT: " + e.getMessage());
            jwt = null;
            filterChain.doFilter(request, response);
            return;
        }


        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userService.findByUsername(username)
                    .orElse(null);

            if (userDetails == null) {
                System.out.println("User details not found for username: " + username);
            } else {
                System.out.println("User details found for: " + username);
            }

            // Only validate token if JWT is not null (i.e., not malformed and successfully retrieved username)
            if (jwt != null && userDetails != null && jwtTokenProvider.validateToken(jwt)) {
                System.out.println("JWT valid. Setting authentication context for: " + username);
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else if (userDetails != null && jwt == null) {
                System.out.println("JWT was malformed or invalid, authentication not set for user: " + username);
            } else if (userDetails != null) {
                System.out.println("JWT invalid for user: " + username);
            }
        }
        filterChain.doFilter(request, response);
    }
}