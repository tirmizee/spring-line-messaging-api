package com.tirmizee.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LineMessagingServiceTest {
    private LineMessagingService lineMessagingService;

    private static final String CHANNEL_ACCESS_TOKEN = "x/OsSKFdh8VCrZaRssEx5v6t5ATUKkc83nOlTeTHbMS4rWPGHK3BFuRWfwDdjao0bdGJwiXkSI1SNZL9Kuo+CCNfKONOmoK1GNmibE+/eBZQlH6gsxZKo/h+f965ws5UVXyUb67FqVpg57J2qRpMrQdB04t89/1O/w1cDnyilFU=";
    private static final String TEST_USER_ID = "Ub9f913cf40ad5df3bc3fe6c107c8ae97";

    @BeforeEach
    void setUp() {
        WebClient lineWebClient = WebClient.builder()
                .baseUrl("https://api.line.me/v2/bot")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + CHANNEL_ACCESS_TOKEN)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        lineMessagingService = new LineMessagingService(lineWebClient);
    }

    @Test
    void testSendPushMessage() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> request = Map.of(
                "to", TEST_USER_ID,
                "messages", List.of(
                        Map.of(
                                "type", "text",
                                "text", "ทดสอบข้อความจาก unit test ✅"
                        )
                )
        );

        JsonNode requestJson = objectMapper.valueToTree(request);

        Mono<JsonNode> response = lineMessagingService.sendPushMessage(requestJson);

        JsonNode result = response.block(); // block for test purposes

        assertNotNull(result);
        System.out.println("📨 LINE API Push Response: " + result.toPrettyString());
    }
}