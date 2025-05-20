package com.capstone7.ptufestival.common.discord;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DiscordNotifier {

    private final String webhookUrl;

    public DiscordNotifier(@Value("${discord.token}") String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    public void sendToDiscord(String content) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String payload = String.format("{\"content\":\"%s\"}", content);

        HttpEntity<String> request = new HttpEntity<>(payload, headers);
        try {
            restTemplate.postForEntity(webhookUrl, request, String.class);
        } catch (Exception e) {
            System.out.println("[디스코드 전송 실패] " + e.getMessage());
        }
    }
}

