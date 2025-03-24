package com.capstone7.ptufestival.controller;

import com.capstone7.ptufestival.jwt.JwtUtil;
import com.capstone7.ptufestival.model.User;
import com.capstone7.ptufestival.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        user.setRole("USER"); // 기본 역할 부여
        userRepository.save(user);
        return "User registered successfully";
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());


        if (existingUser.isEmpty() || !passwordEncoder.matches(user.getPassword(), existingUser.get().getPassword())) {
            return "Invalid username or password";
        }

        return jwtUtil.generateToken(existingUser.get().getUsername());
    }
}

