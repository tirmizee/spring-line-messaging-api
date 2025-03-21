package com.tirmizee.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class LineMessagingService {

    private final WebClient lineWebClient;
    private static final String REPLY_MESSAGE_URL = "/message/reply";
    private static final String PUSH_MESSAGE_URL = "/message/push";
    private static final String MULTICAST_MESSAGE_URL = "/message/multicast";
    private static final String BROADCAST_MESSAGE_URL = "/message/broadcast";
    private static final String NARROWCAST_MESSAGE_URL = "/message/narrowcast";

    public Mono<JsonNode> sendReplyMessage(JsonNode request) {
        return lineWebClient.post()
                .uri(REPLY_MESSAGE_URL)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .doOnSuccess(response -> log.info("LINE API Reply Response: {}", response))
                .doOnError(error -> log.error("Error sending reply message: {}", error.getMessage()));
    }

    public Mono<JsonNode> sendPushMessage(JsonNode request) {
        return lineWebClient.post()
                .uri(PUSH_MESSAGE_URL)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .doOnSuccess(response -> log.info("LINE API Push Response: {}", response))
                .doOnError(error -> log.error("Error sending push message: {}", error.getMessage()));
    }

    public Mono<JsonNode> sendMulticastMessage(JsonNode request) {
        return lineWebClient.post()
                .uri(MULTICAST_MESSAGE_URL)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .doOnSuccess(response -> log.info("LINE API Multicast Response: {}", response))
                .doOnError(error -> log.error("Error sending multicast message: {}", error.getMessage()));
    }

    public Mono<JsonNode> sendBroadcastMessage(JsonNode request) {
        return lineWebClient.post()
                .uri(BROADCAST_MESSAGE_URL)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .doOnSuccess(response -> log.info("LINE API Broadcast Response: {}", response))
                .doOnError(error -> log.error("Error sending broadcast message: {}", error.getMessage()));
    }

    public Mono<JsonNode> sendNarrowcastMessage(JsonNode request) {
        return lineWebClient.post()
                .uri(NARROWCAST_MESSAGE_URL)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .doOnSuccess(response -> log.info("LINE API Narrowcast Response: {}", response))
                .doOnError(error -> log.error("Error sending narrowcast message: {}", error.getMessage()));
    }

}
