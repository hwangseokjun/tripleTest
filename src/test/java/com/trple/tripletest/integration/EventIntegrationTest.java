package com.trple.tripletest.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trple.tripletest.dto.EventActionEnum;
import com.trple.tripletest.dto.EventTypeEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import static com.trple.tripletest.dto.EventActionEnum.*;
import static com.trple.tripletest.dto.EventTypeEnum.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EventIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private HttpHeaders headers;
    private ObjectMapper mapper = new ObjectMapper();

    private final String user1 = UUID.randomUUID().toString();
    private final String user2 = UUID.randomUUID().toString();
    private final String place1 = UUID.randomUUID().toString();
    private final String place2 = UUID.randomUUID().toString();
    private final String review1 = UUID.randomUUID().toString();
    private final String review2 = UUID.randomUUID().toString();
    private final ArrayList<String> photoIds1 = new ArrayList<>(Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString()));
    private final ArrayList<String> photoIds2 = new ArrayList<>();
    private final String content1 = "최고의 여행지에요!";
    private final String content2 = "";

    @BeforeEach
    public void setup() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    // 신규 event 등록 테스트
    @Nested
    @DisplayName("케이스 등록 테스트")
    class RegisterEventTest {
        @Test
        @Order(1)
        @DisplayName("신규 event 등록1 - ADD, 첫리뷰, 내용 1자 이상, 사진 1장 이상")
        void test1() throws JsonProcessingException {
            // given
            EventRequestDto requestDto = EventRequestDto.builder()
                    .userId(user1)
                    .placeId(place1)
                    .reviewId(review1)
                    .action(ADD)
                    .type(REVIEW)
                    .attachedPhotoIds(photoIds1)
                    .content(content1)
                    .build();

            String requestBody = mapper.writeValueAsString(requestDto);
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
            // when
            ResponseEntity<EventRequestDto> response = restTemplate.postForEntity(
                    "/events",
                    request,
                    EventRequestDto.class);
            // then
            assertEquals(HttpStatus.OK, response.getStatusCode());

            EventRequestDto eventResponse = response.getBody();
            assertNotNull(eventResponse);
            assertTrue(eventResponse.id > 0);
            assertEquals(requestDto.type, eventResponse.type);
            assertEquals(requestDto.action, eventResponse.action);
            assertEquals(requestDto.reviewId, eventResponse.reviewId);
            assertEquals(requestDto.content, eventResponse.content);
            assertEquals(requestDto.attachedPhotoIds.toString(), eventResponse.responsePhotoIds);
            assertEquals(requestDto.userId, eventResponse.userId);
            assertEquals(requestDto.placeId, eventResponse.placeId);
            assertEquals(3, eventResponse.point);
        }

        @Test
        @Order(2)
        @DisplayName("신규 event 등록2 - ADD, 첫리뷰")
        void test2() throws JsonProcessingException {
            // given
            EventRequestDto requestDto = EventRequestDto.builder()
                    .userId(user1)
                    .placeId(place2)
                    .reviewId(review1)
                    .action(ADD)
                    .type(REVIEW)
                    .attachedPhotoIds(photoIds2)
                    .content(content2)
                    .build();

            String requestBody = mapper.writeValueAsString(requestDto);
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
            // when
            ResponseEntity<EventRequestDto> response = restTemplate.postForEntity(
                    "/events",
                    request,
                    EventRequestDto.class);
            // then
            assertEquals(HttpStatus.OK, response.getStatusCode());

            EventRequestDto eventResponse = response.getBody();
            assertNotNull(eventResponse);
            assertTrue(eventResponse.id > 0);
            assertEquals(requestDto.type, eventResponse.type);
            assertEquals(requestDto.action, eventResponse.action);
            assertEquals(requestDto.reviewId, eventResponse.reviewId);
            assertEquals(requestDto.content, eventResponse.content);
            assertEquals(requestDto.attachedPhotoIds.toString(), eventResponse.responsePhotoIds);
            assertEquals(requestDto.userId, eventResponse.userId);
            assertEquals(requestDto.placeId, eventResponse.placeId);
            assertEquals(1, eventResponse.point);
        }

        @Test
        @Order(3)
        @DisplayName("신규 event 등록3 - ADD")
        void test3() throws JsonProcessingException {
            // given
            EventRequestDto requestDto = EventRequestDto.builder()
                    .userId(user2)
                    .placeId(place2)
                    .reviewId(review1)
                    .action(ADD)
                    .type(REVIEW)
                    .attachedPhotoIds(photoIds2)
                    .content(content2)
                    .build();

            String requestBody = mapper.writeValueAsString(requestDto);
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
            // when
            ResponseEntity<EventRequestDto> response = restTemplate.postForEntity(
                    "/events",
                    request,
                    EventRequestDto.class);
            // then
            assertEquals(HttpStatus.OK, response.getStatusCode());

            EventRequestDto eventResponse = response.getBody();
            assertNotNull(eventResponse);
            assertTrue(eventResponse.id > 0);
            assertEquals(requestDto.type, eventResponse.type);
            assertEquals(requestDto.action, eventResponse.action);
            assertEquals(requestDto.reviewId, eventResponse.reviewId);
            assertEquals(requestDto.content, eventResponse.content);
            assertEquals(requestDto.attachedPhotoIds.toString(), eventResponse.responsePhotoIds);
            assertEquals(requestDto.userId, eventResponse.userId);
            assertEquals(requestDto.placeId, eventResponse.placeId);
            assertEquals(0, eventResponse.point);
        }

        @Test
        @Order(4)
        @DisplayName("event 중복 등록")
        void test4() throws JsonProcessingException {
            // given
            EventRequestDto requestDto = EventRequestDto.builder()
                    .userId(user1)
                    .placeId(place1)
                    .reviewId(review1)
                    .action(ADD)
                    .type(REVIEW)
                    .attachedPhotoIds(photoIds1)
                    .content(content1)
                    .build();

            String requestBody = mapper.writeValueAsString(requestDto);
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
            // when
            ResponseEntity<EventRequestDto> response = restTemplate.postForEntity(
                    "/events",
                    request,
                    EventRequestDto.class);
            // then
            assertEquals("002", response.getBody().getCode());
        }

        @Test
        @Order(5)
        @DisplayName("event 삭제")
        void test5() throws JsonProcessingException {
            // given
            EventRequestDto requestDto = EventRequestDto.builder()
                    .userId(user1)
                    .placeId(place1)
                    .reviewId(review1)
                    .action(DELETE)
                    .type(REVIEW)
                    .attachedPhotoIds(photoIds1)
                    .content(content1)
                    .build();

            String requestBody = mapper.writeValueAsString(requestDto);
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
            // when
            ResponseEntity<EventRequestDto> response = restTemplate.postForEntity(
                    "/events",
                    request,
                    EventRequestDto.class);
            // then
            assertEquals(HttpStatus.OK, response.getStatusCode());

            EventRequestDto eventResponse = response.getBody();
            assertNotNull(eventResponse);
            assertTrue(eventResponse.id > 0);
            assertEquals(requestDto.type, eventResponse.type);
            assertEquals(requestDto.action, eventResponse.action);
            assertEquals(requestDto.reviewId, eventResponse.reviewId);
            assertEquals(requestDto.content, eventResponse.content);
            assertEquals(requestDto.attachedPhotoIds.toString(), eventResponse.responsePhotoIds);
            assertEquals(requestDto.userId, eventResponse.userId);
            assertEquals(requestDto.placeId, eventResponse.placeId);
            assertEquals(0, eventResponse.point);
        }

        @Test
        @Order(6)
        @DisplayName("event 삭제 후 재등록")
        void test6() throws JsonProcessingException {
            // given
            EventRequestDto requestDto = EventRequestDto.builder()
                    .userId(user1)
                    .placeId(place1)
                    .reviewId(review1)
                    .action(ADD)
                    .type(REVIEW)
                    .attachedPhotoIds(photoIds1)
                    .content(content1)
                    .build();

            String requestBody = mapper.writeValueAsString(requestDto);
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
            // when
            ResponseEntity<EventRequestDto> response = restTemplate.postForEntity(
                    "/events",
                    request,
                    EventRequestDto.class);
            // then
            assertEquals(HttpStatus.OK, response.getStatusCode());

            EventRequestDto eventResponse = response.getBody();
            assertNotNull(eventResponse);
            assertTrue(eventResponse.id > 0);
            assertEquals(requestDto.type, eventResponse.type);
            assertEquals(requestDto.action, eventResponse.action);
            assertEquals(requestDto.reviewId, eventResponse.reviewId);
            assertEquals(requestDto.content, eventResponse.content);
            assertEquals(requestDto.attachedPhotoIds.toString(), eventResponse.responsePhotoIds);
            assertEquals(requestDto.userId, eventResponse.userId);
            assertEquals(requestDto.placeId, eventResponse.placeId);
            assertEquals(3, eventResponse.point);
        }
    }

    @Getter @Setter
    @Builder
    static class EventRequestDto {
        private Long id;
        private EventTypeEnum type;
        private EventActionEnum action;
        private String reviewId;
        private String content;
        private ArrayList<String> attachedPhotoIds;
        private String responsePhotoIds;
        private String userId;
        private String placeId;
        private Integer point;
        private String code;
    }
}