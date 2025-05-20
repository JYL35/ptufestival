package com.capstone7.ptufestival.auth.jwt;

import com.capstone7.ptufestival.auth.model.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    private final SecretKey key;
    private final long expiration;

    private static final ZoneId KOREA_ZONE = ZoneId.of("Asia/Seoul");

    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.expiration}") long expiration) {
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.expiration = expiration;
    }

    public String generateToken(String username, Role role, Long id) {
        ZonedDateTime nowKST = ZonedDateTime.now(KOREA_ZONE);
        Date issuedAt = Date.from(nowKST.toInstant());
        Date expiryAt = Date.from(nowKST.plus(Duration.ofMillis(expiration)).toInstant());

        return Jwts.builder()
                .setSubject(username)
                .addClaims(Map.of("role", role, "userId", id))
                .setIssuedAt(issuedAt)
                .setExpiration(expiryAt)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String extractRole(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
