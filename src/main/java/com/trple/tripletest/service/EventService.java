package com.trple.tripletest.service;

import com.trple.tripletest.dto.EventActionEnum;
import com.trple.tripletest.dto.EventResponseDto;
import com.trple.tripletest.exception.CustomException;
import com.trple.tripletest.model.Place;
import com.trple.tripletest.model.Point;
import com.trple.tripletest.repository.EventRepository;
import com.trple.tripletest.dto.EventRequestDto;
import com.trple.tripletest.model.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.trple.tripletest.dto.EventActionEnum.*;
import static com.trple.tripletest.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class EventService {

    private Place place;
    private EventRequestDto requestDto;
    private final EventRepository eventRepository;

    /* 이벤트 기록 저장 */
    public EventResponseDto saveEvent(EventRequestDto requestDto){
        assignEventRequestDto(requestDto);
        checkEventValidations();
        return createEventAndSave();
    }

    private void assignEventRequestDto(EventRequestDto requestDto){ // EventRequestDto 할당
        this.requestDto = requestDto;
    }

    /* 유효성 검사 */
    private void checkEventValidations() {
        checkValidationByActions();
    }

    private void checkValidationByActions(){ // 리뷰 중복 등록 검증, DELETE가 아닌 경우만.
        findReview();
        checkValidationsWhen();
    }

    private void findReview(){ // 동적 쿼리로 리팩토링 필요. ADD 시에는 placeId와 userId로 검증함
        this.place =  eventRepository
                .findFirstByPlaceIdAndUserIdAndReviewIdOrderByIdDesc(
                        this.requestDto.getPlaceId(),
                        this.requestDto.getUserId(),
                        this.requestDto.getReviewId())
                .orElse(null);
    }

    private void checkValidationsWhen(){
        if (requestDto.getAction().equals(ADD)) checkReviewAlreadyExists();
        else checkReviewNotFound();
    }

    private void checkReviewAlreadyExists() {
        if ( this.place == null ) return;
        if ( !this.place.getAction().equals(DELETE)) throw new CustomException(ALREADY_EXISTS_REVIEW);
    }

    private void checkReviewNotFound() {
        if ( this.place == null ) throw new CustomException(NOT_FOUND_REVIEW);
        if ( this.place.getAction().equals(DELETE)) throw new CustomException(NOT_FOUND_REVIEW);
    }

    private EventResponseDto createEventAndSave(){
        Event log = Event.createWithPoint(this.requestDto, getPoint());
        return EventResponseDto.createFrom(eventRepository.save(log));
    }

    /* 포인트 계산 */
    private int getPoint(){

        switch (this.requestDto.getAction()) {
            case ADD: return getPointWhenAdd();
            case MOD: return getPointWhenMod();
            case DELETE: return getPointWhenDelete();
            default: throw new CustomException(INVALID_ACTION_NAME);
        }
    }

    private int getPointWhenAdd(){ // ADD 일 때의 획득 포인트 계산
        return getPointByContentLength() + getPointByPhotoIdsSize() + getPointWhenFirstReview();
    }

    private int getPointWhenMod(){ // MOD 일 때의 획득 포인트 계산
        return getPointByContentLength() + getPointByPhotoIdsSize();
    }

    private int getPointWhenDelete(){ // DELETE 일 때의 획득 포인트 계산
        return 0;
    }

    private int getPointByContentLength(){ // ContentLength에 따른 보너스 포인트 부여
        if (this.requestDto.getContent().length() >= 1 ) return 1;
        else return 0;
    }

    private int getPointByPhotoIdsSize(){ // PhotoIdsSize에 따른 보너스 포인트 부여
        if (this.requestDto.getAttachedPhotoIds().size() >= 1) return 1;
        else return 0;
    }

    private int getPointWhenFirstReview(){ // 장소에 대한 첫 리뷰 보너스 포인트 부여

        Place place = eventRepository
                .findFirstByPlaceIdOrderByIdDesc(this.requestDto.getPlaceId())
                .orElse(null);

        if ( place == null || place.getAction().equals(DELETE) ) return 1;
        else return 0;
    }

    /* 포인트 총합 계산 */
    public int calculateTotalPoint(String userId){

        List<Point> points = eventRepository.findAllPointByUserId(userId);

        return points
                .stream()
                .filter(p -> !p.getAction().equals(DELETE))
                .mapToInt(Point::getPoint)
                .sum();
    }
}
