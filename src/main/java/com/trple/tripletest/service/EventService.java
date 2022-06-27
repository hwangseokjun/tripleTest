package com.trple.tripletest.service;

import com.trple.tripletest.dto.EventResponseDto;
import com.trple.tripletest.exception.CustomException;
import com.trple.tripletest.projection.ActionProjection;
import com.trple.tripletest.projection.PointProjection;
import com.trple.tripletest.projection.UserIdProjection;
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

    private ActionProjection action;
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
        findReview();
        checkByAction();
    }

    private void checkByAction(){ // 리뷰 중복 등록 검증, DELETE가 아닌 경우만.
        if (requestDto.getAction().equals(ADD)) checkReviewAlreadyExists();
        else checkReviewNotFound();
    }

    private void findReview(){ // 동적 쿼리로 리팩토링 필요. ADD 시에는 placeId와 userId로 검증함
        this.action =  eventRepository
                .findFirstByPlaceIdAndUserIdAndReviewIdOrderByIdDesc(
                        this.requestDto.getPlaceId(),
                        this.requestDto.getUserId(),
                        this.requestDto.getReviewId())
                .orElse(null);
    }

    private void checkReviewAlreadyExists() {
        if ( this.action == null ) return;
        if ( !this.action.getAction().equals(DELETE)) throw new CustomException(ALREADY_EXISTS_REVIEW);
    }

    private void checkReviewNotFound() {
        if ( this.action == null ) throw new CustomException(NOT_FOUND_REVIEW);
        if ( this.action.getAction().equals(DELETE)) throw new CustomException(NOT_FOUND_REVIEW);
    }

    private EventResponseDto createEventAndSave(){
        Event log = Event.createWithPoint(this.requestDto, getPoint());
        return EventResponseDto.createFrom(eventRepository.save(log));
    }

    /* 포인트 계산 */
    private int getPoint(){

        switch (this.requestDto.getAction()) {
            case ADD: return calculatePointWhenAdd();
            case MOD: return calculatePointWhenMod();
            case DELETE: return calculatePointWhenDelete();
            default: throw new CustomException(INVALID_ACTION_NAME);
        }
    }

    private int calculatePointWhenAdd(){ // 획득 포인트 계산
        return addPointByContentLength() + addPointByPhotoIdsSize() + addPointWhenAddFirst();
    }

    private int calculatePointWhenMod() {
        return addPointByContentLength() + addPointByPhotoIdsSize() + addPointWhenModFirstReview();
    }

    private int calculatePointWhenDelete(){ // DELETE 일 때의 획득 포인트 계산
        return 0;
    }

    private int addPointByContentLength(){ // ContentLength에 따른 보너스 포인트 부여
        if (this.requestDto.getContent().length() >= 1 ) return 1;
        else return 0;
    }

    private int addPointByPhotoIdsSize(){ // PhotoIdsSize에 따른 보너스 포인트 부여
        if (this.requestDto.getAttachedPhotoIds().size() >= 1) return 1;
        else return 0;
    }

    private int addPointWhenAddFirst(){ // 장소에 대한 첫 리뷰 보너스 포인트 부여

        List<ActionProjection> reviews = eventRepository
                .findFirstByPlaceIdGroupByUser(this.requestDto.getPlaceId());

        if ( reviews.size() == 0 ) return 1;

        for ( ActionProjection review : reviews ) {
            if ( review.getAction().equals(DELETE) ) return 1;
        }

        return 0;
    }

    private int addPointWhenModFirstReview() {
        // 첫 댓글에 대한 수정인지 아닌지 판단해야 함.
        UserIdProjection userId = eventRepository
                .findFirstByPlaceIdAndActionNotOrderByIdAsc(this.requestDto.getPlaceId(), DELETE)
                .orElseThrow( () -> new CustomException(NOT_FOUND_REVIEW));

        if ( userId.getUserId().equals(requestDto.getUserId())) return 1;

        return 0;
    }

    /* 포인트 총합 계산 */
    public int calculateTotalPoint(String userId){

        List<PointProjection> points = eventRepository.findAllPointByUserId(userId);

        return points
                .stream()
                .filter(p -> !p.getAction().equals(DELETE))
                .mapToInt(PointProjection::getPoint)
                .sum();
    }
}
