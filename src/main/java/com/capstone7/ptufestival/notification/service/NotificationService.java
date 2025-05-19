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

    public SseEmitter createEmitter(String clientId) {
        // 기존 emitter가 있다면 제거
        if (emitters.containsKey(clientId)) {
            SseEmitter oldEmitter = emitters.get(clientId);
            oldEmitter.complete();
            emitters.remove(clientId);
            System.out.println("[SSE 재연결] 기존 emitter 제거: " + clientId);
        }

        // 새로운 emitter 생성
        SseEmitter emitter = new SseEmitter(3 * 60 * 1000L); // 3분 유지
        emitters.put(clientId, emitter);
        System.out.println("[SSE 연결] emitter 등록: " + clientId);

        emitter.onCompletion(() -> {
            emitters.remove(clientId);
            System.out.println("[SSE 완료] emitter 제거됨: " + clientId);
        });

        emitter.onTimeout(() -> {
            emitters.remove(clientId);
            System.out.println("[SSE 타임아웃] emitter 제거됨: " + clientId);
        });

        emitter.onError(e -> {
            emitters.remove(clientId);
            System.out.println("[SSE 에러] emitter 제거됨: " + clientId);
        });

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

    // Discord 알림용
    public int getEmitterCount() {
        return emitters.size();
    }
}
