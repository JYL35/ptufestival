package com.capstone7.ptufestival.notification.controller;

import com.capstone7.ptufestival.auth.model.User;
import com.capstone7.ptufestival.common.dto.ApiResponse;
import com.capstone7.ptufestival.notification.dto.NotificationRequestDto;
import com.capstone7.ptufestival.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/notification")
@Tag(name = "긴급 알림", description = "SSE 실시간 알림 API")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "SSE 연결", description = "클라이언트가 접속 시 SSE 연결을 맺습니다.")
    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connect() {
        return notificationService.createEmitter();
    }

    @Operation(summary = "알림 전송", description = "관리자 권한을 가진 클라이언트가 알림을 전송합니다.")
    @PostMapping(value = "/send", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> sendNotification(@RequestBody NotificationRequestDto dto, @AuthenticationPrincipal User user) {
        notificationService.sendToAll(dto);
        return ApiResponse.success("알림이 전송되었습니다.");
    }
}
