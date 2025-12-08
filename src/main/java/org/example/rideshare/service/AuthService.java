package org.example.rideshare.service;

import org.example.rideshare.dto.LoginRequest;
import org.example.rideshare.dto.LoginResponse;
import org.example.rideshare.dto.RegisterRequest;
import org.example.rideshare.exception.BadRequestException;
import org.example.rideshare.model.User;
import org.example.rideshare.repository.UserRepository;
import org.example.rideshare.util.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public void register(RegisterRequest request) {
        // Check if user already exists
        Optional<User> existingUser = userRepository.findByUsername(request.getUsername());
        if (existingUser.isPresent()) {
            throw new BadRequestException("Username already exists");
        }

        // Validate role
        if (!request.getRole().equals("ROLE_USER") && !request.getRole().equals("ROLE_DRIVER")) {
            throw new BadRequestException("Role must be ROLE_USER or ROLE_DRIVER");
        }

        // Create new user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {
        // Find user
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadRequestException("Invalid username or password"));

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid username or password");
        }

        // Generate JWT token
        String token = jwtTokenProvider.generateToken(user.getUsername(), user.getRole());

        return new LoginResponse(token, user.getUsername(), user.getRole());
    }
}