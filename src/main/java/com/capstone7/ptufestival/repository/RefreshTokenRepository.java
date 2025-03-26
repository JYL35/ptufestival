// repository/RefreshTokenRepository.java
package com.capstone7.ptufestival.repository;

import com.capstone7.ptufestival.model.RefreshToken;
import com.capstone7.ptufestival.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);
}
