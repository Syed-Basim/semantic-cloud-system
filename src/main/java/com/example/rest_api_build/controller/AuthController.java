package com.example.rest_api_build.controller;

import com.example.rest_api_build.dto.AuthRequestDTO;
import com.example.rest_api_build.entity.User;
import com.example.rest_api_build.repository.UserRepository;
import com.example.rest_api_build.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    // SIGNUP API
    @PostMapping("/signup")
    public String signup(@RequestBody AuthRequestDTO request) {

        // Check if user already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return "User already exists";
        }

        User user = new User();

        user.setEmail(request.getEmail());

        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        userRepository.save(user);

        return "User registered successfully";
    }

    // LOGIN API
    @PostMapping("/login")
    public String login(@RequestBody AuthRequestDTO request) {

        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            return jwtUtil.generateToken(request.getEmail());

        } catch (Exception e) {

            e.printStackTrace();

            return "LOGIN FAILED: " + e.getMessage();
        }
    }
}