// dto/LoginResponseDto.java
package com.capstone7.ptufestival.auth.dto;

import com.capstone7.ptufestival.auth.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {
    private String accessToken;
    private String refreshToken;
    private String username;
    private String role;

    public LoginResponseDto(String accessToken, String refreshToken, String username, Role role) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.username = username;
        this.role = role.name(); // Enum → String 변환
    }
}
