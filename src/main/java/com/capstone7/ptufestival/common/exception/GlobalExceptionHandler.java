// auth/exception/GlobalExceptionHandler.java
package com.capstone7.ptufestival.common.exception;

import com.capstone7.ptufestival.common.discord.DiscordNotifier;
import com.capstone7.ptufestival.common.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final DiscordNotifier discordNotifier;

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgument(IllegalArgumentException e) {
        return ApiResponse.error(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<Object>> handleUnauthorized(UnauthorizedException e) {
        return ApiResponse.error(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiResponse<Object>> handleForbidden(ForbiddenException e) {
        return ApiResponse.error(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNotFound(ResourceNotFoundException e) {
        return ApiResponse.error(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> handleRuntime(RuntimeException e, HttpServletRequest request) {
        discordNotifier.sendToDiscord("[❗런타임 예외 발생]\n" +
                "요청 URI: " + request.getRequestURI() + "\n" +
                "에러 메시지: " + e.getMessage());
        return ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleInternalServerError(
            Exception e,
            HttpServletRequest request
    ) {
        String acceptHeader = request.getHeader("Accept");

        if ("text/event-stream".equalsIgnoreCase(acceptHeader)) {
            // SSE 요청은 빈 응답
            return ResponseEntity.noContent().build();  // 204
        }

        discordNotifier.sendToDiscord("[❗서버 예외 발생]\n" +
                "요청 URI: " + request.getRequestURI() + "\n" +
                "에러 메시지: " + e.getMessage());

        return ApiResponse.error("서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiResponse<Object>> handleNullPointer(NullPointerException e) {
        return ApiResponse.error("서버 오류가 발생했습니다. (NPE)", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<?> buildErrorResponse(String message, HttpStatus status) {
        return ResponseEntity.status(status).body(
                Map.of(
                        "data", null,
                        "message", message,
                        "statusCode", status.value()
                )
        );
    }
}

