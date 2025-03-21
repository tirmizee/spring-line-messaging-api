package com.tirmizee.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Configuration
public class LineMessagingClientConfig {

    @Value("${line.bot.channel-token}")
    private String channelToken;

    @Value("${line.bot.api-url}")
    private String lineApiBaseUrl;

    @PostConstruct
    public void init() {
        log.info("LINE API Base URL: {}", lineApiBaseUrl);
        log.info("LINE Channel Token: {}", channelToken);
    }

    @Bean
    public WebClient lineWebClient() {
        return WebClient.builder()
                .baseUrl(lineApiBaseUrl)  // ใช้ Property แทนค่าคงที่
                .defaultHeader("Authorization", "Bearer " + channelToken)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

}
