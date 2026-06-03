package com.example.rest_api_build.config;

import com.example.rest_api_build.service.CustomUserDetailsService;
import com.example.rest_api_build.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("=== JWT FILTER HIT ===");

        final String authHeader = request.getHeader("Authorization");

        String email = null;
        String token = null;

        System.out.println("HEADER: " + authHeader);

        // Extract token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            System.out.println("TOKEN: " + token);

            try {
                email = jwtUtil.extractEmail(token);
                System.out.println("EMAIL: " + email);
            } catch (Exception e) {
                System.out.println("ERROR extracting email: " + e.getMessage());
            }
        }

        // Validate and authenticate
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            System.out.println("DB USER: " + userDetails.getUsername());

            if (email.equals(userDetails.getUsername())) {

                System.out.println("USER MATCHED ");

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                System.out.println("USER MISMATCH ");
            }
        }

        filterChain.doFilter(request, response);
    }
}