// controller/UserController.java
package com.capstone7.ptufestival.auth.controller;

import com.capstone7.ptufestival.auth.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@Tag(name = "사용자", description = "로그인된 사용자 정보 확인 API")
public class UserController {

    @GetMapping("/me")
    @Operation(
            summary = "현재 로그인한 사용자 조회",
            description = "JWT accessToken을 Authorization 헤더에 담아 요청하면 현재 로그인한 사용자명을 반환합니다.",
            security = @SecurityRequirement(name = "accessToken")
    )
    public String getCurrentUser(@AuthenticationPrincipal User user) {
        return "현재 로그인한 유저: " + user.getUsername();
    }
}
