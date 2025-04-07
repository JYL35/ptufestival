// service/UserService.java
package com.capstone7.ptufestival.auth.service;

import com.capstone7.ptufestival.auth.dto.LoginRequestDto;
import com.capstone7.ptufestival.auth.dto.LoginResponseDto;
import com.capstone7.ptufestival.auth.dto.RegisterRequestDto;
import com.capstone7.ptufestival.auth.jwt.JwtUtil;
import com.capstone7.ptufestival.auth.model.User;
import com.capstone7.ptufestival.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    public void register(RegisterRequestDto request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .role("ROLE_USER")
                .build();

        userRepository.save(user);
    }

    public LoginResponseDto login(LoginRequestDto request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        String accessToken = jwtUtil.generateToken(user.getUsername(), user.getRole(), user.getId());
        String refreshToken = refreshTokenService.createRefreshToken(user).getToken();

        return new LoginResponseDto(accessToken, refreshToken, user.getUsername(), user.getRole());
    }
}
