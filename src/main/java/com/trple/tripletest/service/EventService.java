package com.trple.tripletest.service;

import com.trple.tripletest.exception.CustomException;
import com.trple.tripletest.model.PointLog;
import com.trple.tripletest.repository.EventLogRepository;
import com.trple.tripletest.dto.EventRequestDto;
import com.trple.tripletest.model.EventLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.trple.tripletest.dto.EventActionEnum.*;
import static com.trple.tripletest.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class EventService {

    private EventRequestDto requestDto;
    private final EventLogRepository eventLogRepository;

    //////////////////////// 이벤트 기록 저장 ////////////////////////
    public void saveEvent(EventRequestDto requestDto){
        setEventRequestDto(requestDto);
        isValidEvent(); // 유효성 체크
        EventLog log = EventLog.createWithPoint(requestDto, calculatePoint()); // 로그를 만든다
        eventLogRepository.save(log); // 저장한다
    }

    private void setEventRequestDto(EventRequestDto requestDto){
        this.requestDto = requestDto;
    }

    //////////////////////// 유효성 검사 ////////////////////////
    private void isValidEvent(){
        isValidType();
        isExistsReview();
    }

    private void isValidType(){
        if ( this.requestDto.getType() == null )
            throw new CustomException(NOT_FOUND_EVENT_TYPE);
    }

    private void isExistsReview(){
        if ( eventLogRepository.existsByPlaceIdAndUserId(
                this.requestDto.getPlaceId(),
                this.requestDto.getUserId())
        )
            throw new CustomException(ALREADY_EXISTS_REVIEW);// 다시 설계해야함, ADD 되었을 때의 예외 처리가 정확히 되어 있지 않다.
    }

    //////////////////////// 포인트 계산 ////////////////////////
    private int calculatePoint(){

        switch (this.requestDto.getAction()) {
            case ADD:
                return calculateAddType();
            case DELETE:
                return calculateDeleteType();
            case MOD:
                return calculateModType();
            default:
                throw new CustomException(INVALID_ACTION_NAME);
        }
    }

    private int calculateAddType(){
        return getPointByFirstReview() + getPointByContentLenth() + getPointByPhotoIdsSize();
    }

    private int calculateDeleteType(){
        return 0;
    }

    private int calculateModType(){
        return getPointByContentLenth() + getPointByPhotoIdsSize();
    }

    private int getPointByFirstReview(){
        if (eventLogRepository
                .existsByPlaceIdAndActionNot(
                        this.requestDto.getPlaceId(),
                        this.requestDto.getAction())){
            return 1;
        } else {
            return 0;
        }
    }

    private int getPointByContentLenth(){
        if (this.requestDto.getContent().length() >= 1 ) { return 1; }
        else { return 0; }
    }

    private int getPointByPhotoIdsSize(){
        if (this.requestDto.getAttachedPhotoIds().size() >= 1) { return 1; }
        else { return 0; }
    }

    // 포인트 총합 계산
    public int getTotalPoint(String userId){ // DB 조회 시에 특정 값만 가져올 수 있는지?
        // 중복이 없이, 해당 reviewId 중 가장 최근의 값만 가져올 수 있어야 한다.
        List<PointLog> points = eventLogRepository.findAllPointByUserId(userId);

        return points
                .stream()
                .filter(p -> !p.getAction().equals(DELETE))
                .mapToInt(PointLog::getPoint)
                .sum();
    }



}
