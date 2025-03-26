// dto/LoginResponseDto.java
package com.capstone7.ptufestival.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {
    private String accessToken;
    private String refreshToken;
    private String username;
    private String role;
}
