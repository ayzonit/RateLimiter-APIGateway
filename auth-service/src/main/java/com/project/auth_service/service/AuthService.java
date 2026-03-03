package com.project.auth_service.service;

import com.project.auth_service.Exception.InvalidCredentialsException;
import com.project.auth_service.Exception.UsernameAlreadyExistsException;
import com.project.auth_service.model.Role;
import com.project.auth_service.model.User;
import com.project.auth_service.payload.AuthResponse;
import com.project.auth_service.payload.LoginRequest;
import com.project.auth_service.payload.RegisterRequest;
import com.project.auth_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtService jwtService;

    public AuthResponse register(RegisterRequest registerRequest) {
        if(userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException("Username already taken");
        }

        User user = User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.User)
                .build();

        userRepository.save(user);
        String token = jwtService.generateToken(user);

        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }
}
