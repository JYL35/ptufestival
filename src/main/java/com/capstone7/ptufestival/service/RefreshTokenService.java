// service/RefreshTokenService.java
package com.capstone7.ptufestival.service;

import com.capstone7.ptufestival.model.RefreshToken;
import com.capstone7.ptufestival.model.User;
import com.capstone7.ptufestival.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    // 7일짜리 토큰 생성
    @Transactional
    public RefreshToken createRefreshToken(User user) {
        // 기존 토큰 삭제 (1인 1토큰 방식)
        refreshTokenRepository.deleteByUser(user);

        return refreshTokenRepository.save(RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString()) // 랜덤 문자열
                .expiryDate(LocalDateTime.now().plusDays(7))
                .build());
    }

    public boolean isValid(RefreshToken refreshToken) {
        return refreshToken.getExpiryDate().isAfter(LocalDateTime.now());
    }

    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("리프레시 토큰이 유효하지 않습니다."));
    }
}
