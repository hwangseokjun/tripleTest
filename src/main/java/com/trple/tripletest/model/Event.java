package com.trple.tripletest.model;

import com.trple.tripletest.utils.EventValidator;
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
        @Index(name = "idx_user", columnList = "userId"),
        @Index(name = "idx_review", columnList = "reviewId")})
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 36)
    private String userId;

    @Column(nullable = false, length = 36)
    private String placeId;

    @Column(nullable = false, length = 36)
    private String reviewId;

    @Column(length = 1000)
    private String attachedPhotoIds;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private EventTypeEnum type;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private EventActionEnum action;

    @Column(length = 500)
    private String content;

    @Column(nullable = false)
    private Integer point;

    @CreatedDate
    private LocalDateTime createdAt;

    @Version
    private Integer version;

    public static Event createWithPoint(EventRequestDto requestDto, int point){

        EventValidator validator = new EventValidator(requestDto);
        validator.checkEventValidation();

        return Event.builder()
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
