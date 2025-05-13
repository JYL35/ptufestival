package com.capstone7.ptufestival.notification.service;

import com.capstone7.ptufestival.notification.dto.NotificationRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    // 현재 연결된 클라이언트 저장
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter createEmitter() {
        SseEmitter emitter = new SseEmitter(0L); // 무제한 연결 유지
        String emitterId = UUID.randomUUID().toString();
        emitters.put(emitterId, emitter);

        emitter.onCompletion(() -> emitters.remove(emitterId));
        emitter.onTimeout(() -> emitters.remove(emitterId));
        emitter.onError(e -> emitters.remove(emitterId));

        return emitter;
    }

    public void sendToAll(NotificationRequestDto dto) {
        emitters.forEach((id, emitter) -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("notification")
                        .data(dto)); // ApiResponse 없이 바로 dto 전송
            } catch (IOException e) {
                emitter.completeWithError(e);
                log.warn("SSE 전송 실패 (ID: {}): {}", id, e.getMessage());
            }
        });
    }
}
