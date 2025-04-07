// controller/UserController.java
package com.capstone7.ptufestival.auth.controller;

import com.capstone7.ptufestival.auth.model.User;
import com.capstone7.ptufestival.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<String>> getCurrentUser(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ApiResponse.error("인증된 사용자 정보가 없습니다.", HttpStatus.UNAUTHORIZED);
        }
        return ApiResponse.success("현재 로그인한 유저: " + user.getUsername());
    }
}
