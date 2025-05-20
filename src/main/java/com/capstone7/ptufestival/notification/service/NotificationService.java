package com.capstone7.ptufestival.notification.service;

import com.capstone7.ptufestival.common.discord.DiscordNotifier;
import com.capstone7.ptufestival.notification.dto.NotificationRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    // 현재 연결된 클라이언트 저장
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final DiscordNotifier discordNotifier;

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
        int successCount = 0;
        int failCount = 0;

        for (Map.Entry<String, SseEmitter> entry : emitters.entrySet()) {
            String id = entry.getKey();
            SseEmitter emitter = entry.getValue();
            try {
                emitter.send(SseEmitter.event()
                        .name("notification")
                        .data(dto));
                successCount++;
            } catch (IOException e) {
                emitter.completeWithError(e);
                emitters.remove(id);
                failCount++;
            }
        }

        String message = String.format("[📢 공지 전송] 제목: '%s', 전달 성공: %d명, 실패: %d명", dto.getTitle(), successCount, failCount);
        discordNotifier.sendToDiscord(message);
    }

    public void getEmitterCount() {
        int count = emitters.size();
        discordNotifier.sendToDiscord("[🔔 접속 중인 사용자 수(SSE)] " + count + "명");
    }
}
