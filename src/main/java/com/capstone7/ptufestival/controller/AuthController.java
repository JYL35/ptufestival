// controller/AuthController.java
package com.capstone7.ptufestival.controller;

import com.capstone7.ptufestival.dto.LoginRequestDto;
import com.capstone7.ptufestival.dto.LoginResponseDto;
import com.capstone7.ptufestival.dto.RegisterRequestDto;
import com.capstone7.ptufestival.jwt.JwtUtil;
import com.capstone7.ptufestival.model.RefreshToken;
import com.capstone7.ptufestival.model.User;
import com.capstone7.ptufestival.service.RefreshTokenService;
import com.capstone7.ptufestival.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDto request) {
        userService.register(request);
        return ResponseEntity.ok("회원가입 완료");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(userService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        String refreshTokenStr = request.get("refreshToken");

        RefreshToken refreshToken = refreshTokenService.findByToken(refreshTokenStr);
        if (!refreshTokenService.isValid(refreshToken)) {
            return ResponseEntity.badRequest().body("Refresh token has expired");
        }

        User user = refreshToken.getUser();
        String newAccessToken = jwtUtil.generateToken(user.getUsername(), user.getRole(), user.getId());

        return ResponseEntity.ok(Map.of(
                "accessToken", newAccessToken
        ));
    }
}
