package com.trple.tripletest.dto;

import lombok.Getter;

import java.util.ArrayList;

@Getter
public class EventRequestDto {

    private EventTypeEnum type;
    private EventActionEnum action;
    private String reviewId;
    private String content;
    private ArrayList<String> attachedPhotoIds;
    private String userId;
    private String placeId;

}
