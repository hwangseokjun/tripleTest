package com.trple.tripletest.model;

import com.trple.tripletest.dto.EventActionEnum;
import com.trple.tripletest.dto.EventRequestDto;
import com.trple.tripletest.dto.EventTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor @AllArgsConstructor
@Table(indexes = {
        @Index(name = "idx_place", columnList = "placeId"),
        @Index(name = "idx_user", columnList = "userId")})
public class EventLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private String placeId;

    private String reviewId;

    @Column(length = 1000)
    private String attachedPhotoIds;

    @Enumerated(value = EnumType.STRING)
    private EventTypeEnum type;

    @Enumerated(value = EnumType.STRING)
    private EventActionEnum action;

    private String content;

    private Integer point;

    @CreatedDate
    private LocalDateTime createdAt;

    @Version
    private Integer version;

    public static EventLog createWithPoint(EventRequestDto requestDto, int point){

        return EventLog.builder()
                .userId(requestDto.getUserId())
                .placeId(requestDto.getPlaceId())
                .reviewId(requestDto.getReviewId())
                .attachedPhotoIds(requestDto.getAttachedPhotoIds().toString())
                .type(requestDto.getType())
                .action(requestDto.getAction())
                .content(requestDto.getContent())
                .point(point)
                .build();
    }

}
