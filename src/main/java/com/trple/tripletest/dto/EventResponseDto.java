package com.trple.tripletest.dto;

import com.trple.tripletest.model.Event;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EventResponseDto {

    private Long id;
    private EventTypeEnum type;
    private EventActionEnum action;
    private String reviewId;
    private String content;
    private String responsePhotoIds;
    private String userId;
    private String placeId;
    private Integer point;

    public static EventResponseDto createFrom(Event event) {
        return EventResponseDto.builder()
                .id(event.getId())
                .type(event.getType())
                .action(event.getAction())
                .reviewId(event.getReviewId())
                .content(event.getContent())
                .responsePhotoIds(event.getAttachedPhotoIds())
                .userId(event.getUserId())
                .placeId(event.getPlaceId())
                .point(event.getPoint())
                .build();
    }

}
