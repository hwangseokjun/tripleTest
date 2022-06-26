package com.trple.tripletest.model;

import com.trple.tripletest.dto.EventActionEnum;
import com.trple.tripletest.dto.EventRequestDto;
import com.trple.tripletest.dto.EventTypeEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;

import static com.trple.tripletest.dto.EventActionEnum.*;
import static com.trple.tripletest.dto.EventTypeEnum.*;
import static org.junit.jupiter.api.Assertions.*;

class EventLogTest {
    @Test // JUnit으로 테스트를 시작하는 어노테이션
    @DisplayName("케이스 입력") // 검증결과에 대한 네임텍
    void createEventLogNormal(){
        // given
        String userId = UUID.randomUUID().toString();
        String placeId = UUID.randomUUID().toString();
        String reviewId = UUID.randomUUID().toString();
        ArrayList<String> attachedPhotoIds = new ArrayList<>();
        EventTypeEnum type = REVIEW;
        EventActionEnum action = ADD;
        String content = "너무 멋진 곳이에요!";

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
        EventLog eventLog = EventLog.createWithPoint(requestDto, 0); // 계산에 대한 테스트 케이스를 정확히 입력해 주어야 합니다.

        // then
        assertNull(eventLog.getId());
        assertNull(eventLog.getCreatedAt());
        assertNull(eventLog.getVersion());
        assertEquals(userId, eventLog.getUserId());
        assertEquals(placeId, eventLog.getPlaceId());
        assertEquals(reviewId, eventLog.getReviewId());
        assertEquals(attachedPhotoIds.toString(), eventLog.getAttachedPhotoIds());
        assertEquals(type, eventLog.getType());
        assertEquals(action, eventLog.getAction());
        assertEquals(content, eventLog.getContent());
        assertEquals(0, eventLog.getPoint()); // 계산 작동 여부에 대한 테스트가 필요합니다.
    }

}
