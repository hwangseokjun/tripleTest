package com.trple.tripletest.unit;

import com.trple.tripletest.dto.*;
import com.trple.tripletest.exception.CustomException;
import com.trple.tripletest.model.Event;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.UUID;

import static com.trple.tripletest.dto.EventActionEnum.*;
import static com.trple.tripletest.dto.EventTypeEnum.*;
import static org.junit.jupiter.api.Assertions.*;

class EvenTest {

    private String userId;
    private String placeId;
    private String reviewId;
    private ArrayList<String> attachedPhotoIds;
    private EventTypeEnum type;
    private EventActionEnum action;
    private String content;

    @BeforeEach // 모든 테스트에서 한 번 시행한 후 적용됨
    void setup() {
        this.userId = UUID.randomUUID().toString();
        this.placeId = UUID.randomUUID().toString();
        this.reviewId = UUID.randomUUID().toString();
        this.attachedPhotoIds = new ArrayList<>();
        this.type = REVIEW;
        this.action = ADD;
        this.content = "너무 멋진 곳이에요!";
    }

    @Test // JUnit으로 테스트를 시작하는 어노테이션
    @DisplayName("정상 케이스") // 검증결과에 대한 네임텍
    void createEventNormal() {
        // given
        EventRequestDto requestDto = EventRequestDto
                .builder()
                .userId(userId)
                .placeId(placeId)
                .reviewId(reviewId)
                .attachedPhotoIds(attachedPhotoIds)
                .type(type)
                .action(action)
                .content(content)
                .build();
        // when
        Event event = Event.createWithPoint(requestDto, 0); // 계산에 대한 테스트 케이스를 정확히 입력해 주어야 합니다.

        // then
        assertNull(event.getId());
        assertNull(event.getCreatedAt());
        assertNull(event.getVersion());
        assertEquals(userId, event.getUserId());
        assertEquals(placeId, event.getPlaceId());
        assertEquals(reviewId, event.getReviewId());
        assertEquals(attachedPhotoIds.toString(), event.getAttachedPhotoIds());
        assertEquals(type, event.getType());
        assertEquals(action, event.getAction());
        assertEquals(content, event.getContent());
        assertEquals(0, event.getPoint()); // 계산 작동 여부에 대한 테스트가 필요합니다.
    }

    @Nested
    @DisplayName("비정상 케이스")
    class CreateFail {

        @Test
        @DisplayName("유저 ID")
        void userId(){
            // given
            userId = "오류케이스";
            EventRequestDto requestDto = EventRequestDto
                    .builder()
                    .userId(userId)
                    .placeId(placeId)
                    .reviewId(reviewId)
                    .attachedPhotoIds(attachedPhotoIds)
                    .type(type)
                    .action(action)
                    .content(content)
                    .build();
            // when
            CustomException exception = assertThrows(CustomException.class, () -> {
                Event.createWithPoint(requestDto, 0);
            });
            // then
            assertEquals("005", exception.getErrorCode().getCode());
        }

        @Test
        @DisplayName("장소 ID")
        void placeId(){
            // given
            placeId = "오류케이스";
            EventRequestDto requestDto = EventRequestDto
                    .builder()
                    .userId(userId)
                    .placeId(placeId)
                    .reviewId(reviewId)
                    .attachedPhotoIds(attachedPhotoIds)
                    .type(type)
                    .action(action)
                    .content(content)
                    .build();
            // when
            CustomException exception = assertThrows(CustomException.class, () -> {
                Event.createWithPoint(requestDto, 0);
            });
            // then
            assertEquals("005", exception.getErrorCode().getCode());
        }

        @Test
        @DisplayName("리뷰 ID")
        void reviewId(){
            // given
            reviewId = "오류케이스";
            EventRequestDto requestDto = EventRequestDto
                    .builder()
                    .userId(userId)
                    .placeId(placeId)
                    .reviewId(reviewId)
                    .attachedPhotoIds(attachedPhotoIds)
                    .type(type)
                    .action(action)
                    .content(content)
                    .build();
            // when
            CustomException exception = assertThrows(CustomException.class, () -> {
                Event.createWithPoint(requestDto, 0);
            });
            // then
            assertEquals("005", exception.getErrorCode().getCode());
        }

        @Test
        @DisplayName("사진 ID")
        void photoIds(){
            // given
            attachedPhotoIds.add("오류케이스");
            EventRequestDto requestDto = EventRequestDto
                    .builder()
                    .userId(userId)
                    .placeId(placeId)
                    .reviewId(reviewId)
                    .attachedPhotoIds(attachedPhotoIds)
                    .type(type)
                    .action(action)
                    .content(content)
                    .build();
            // when
            CustomException exception = assertThrows(CustomException.class, () -> {
                Event.createWithPoint(requestDto, 0);
            });
            // then
            assertEquals("005", exception.getErrorCode().getCode());
        }

        @Test
        @DisplayName("타입")
        void type(){
            // given
            type = null;
            EventRequestDto requestDto = EventRequestDto
                    .builder()
                    .userId(userId)
                    .placeId(placeId)
                    .reviewId(reviewId)
                    .attachedPhotoIds(attachedPhotoIds)
                    .type(type)
                    .action(action)
                    .content(content)
                    .build();
            // when
            CustomException exception = assertThrows(CustomException.class, () -> {
                Event.createWithPoint(requestDto, 0);
            });
            // then
            assertEquals("003", exception.getErrorCode().getCode());
        }

    }

}