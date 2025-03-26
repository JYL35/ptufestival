// dto/RegisterRequestDto.java
package com.capstone7.ptufestival.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDto {
    private String username;
    private String password;
    private String name;
}
