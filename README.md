# 📦 LINE Messaging API Integration (Spring Boot WebFlux)

โปรเจกต์นี้เป็นตัวอย่างการเชื่อมต่อกับ [LINE Messaging API](https://developers.line.biz/en/services/messaging-api/) ด้วย Spring Boot และ WebClient (Reactive)

ใช้สำหรับส่งข้อความประเภทต่าง ๆ เช่น:
- ✅ Push Message
- 💬 Reply Message
- 📢 Broadcast Message
- 👥 Multicast Message

## ⚙️ Prerequisites

- Java 17+
- Gradle หรือ Maven
- LINE Developer Account
- Channel Access Token (จาก LINE Messaging API)

## 🔧 การตั้งค่า

### 1. เพิ่ม Channel Access Token ใน `application.yml` หรือ `.properties`

```yaml
spring.application.name: spring-line-messaging-api
line.bot:
  api-url: https://api.line.me/v2/bot
  channel-token: YOUR_LINE_CHANNEL_ACCESS_TOKEN
```

### 2. สร้าง WebClient Bean

```java

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

```

### 3. สร้าง Service Bean

```java
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
```

### 4. สร้าง Unit Test

```java

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

```

5. Demo

```text
11:45:34.646 [reactor-http-nio-2] INFO com.tirmizee.service.LineMessagingService -- LINE API Push Response: {"sentMessages":[{"id":"553146540345262658","quoteToken":"dnqTAHAvQDjoR-ednvXKIeqrIA17haENrYy0nYj3TfP9Ywk89v6ClgJZrWUBAIxSWf6JkYXbbjTDeTXZOIO7LjxQTlFmRrsnSPH_1q1SZNDTHMp7GFQpQZGM2uQlln4gSPHU4EyEVrOvSx3mBpJ3pw"}]}
📨 LINE API Push Response: {
  "sentMessages" : [ {
    "id" : "553146540345262658",
    "quoteToken" : "dnqTAHAvQDjoR-ednvXKIeqrIA17haENrYy0nYj3TfP9Ywk89v6ClgJZrWUBAIxSWf6JkYXbbjTDeTXZOIO7LjxQTlFmRrsnSPH_1q1SZNDTHMp7GFQpQZGM2uQlln4gSPHU4EyEVrOvSx3mBpJ3pw"
  } ]
}
> Task :test
BUILD SUCCESSFUL in 32s
4 actionable tasks: 4 executed
11:45:35: Execution finished ':test --tests "com.tirmizee.service.LineMessagingServiceTest"'.

```