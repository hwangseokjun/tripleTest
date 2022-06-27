package com.trple.tripletest.utils;

import com.trple.tripletest.dto.EventRequestDto;
import com.trple.tripletest.exception.CustomException;

import java.util.ArrayList;

import static com.trple.tripletest.exception.ErrorCode.*;

public class EventValidator {

    private final EventRequestDto requestDto;
    private final String regex = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";

    public EventValidator(EventRequestDto requestDto){
        this.requestDto = requestDto;
    }

    public void checkEventValidation(){ // Id값들의 유효성 검증
        checkIdValidations();
        checkTypeValidation();
    }

    private void checkIdValidations(){
        checkValidationOf(this.requestDto.getUserId());
        checkValidationOf(this.requestDto.getPlaceId());
        checkValidationOf(this.requestDto.getReviewId());
        checkValidationOfPhotos(this.requestDto.getAttachedPhotoIds());
    }

    private void checkValidationOf(String uuid){ // UUID 검증
        if ( !uuid.matches(this.regex) ) throw new CustomException(INVALID_UUID);
    }

    private void checkValidationOfPhotos(ArrayList<String> attachedPhotoIds){
        for ( String attachedPhotoId : attachedPhotoIds) checkValidationOf(attachedPhotoId);
    }

    private void checkTypeValidation(){ // EventType 검증
        if ( this.requestDto.getType() == null )
            throw new CustomException(NOT_FOUND_EVENT_TYPE);
    }

}
