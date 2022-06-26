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

    /* 이벤트 기록 저장 */
    public void saveEvent(EventRequestDto requestDto){
        assignEventRequestDto(requestDto);
        checkEventValidation();
        EventLog log = EventLog.createWithPoint(requestDto, getPoint());
        eventLogRepository.save(log);
    }

    private void assignEventRequestDto(EventRequestDto requestDto){ // EventRequestDto 할당
        this.requestDto = requestDto;
    }

    /* 유효성 검사 */
    private void checkEventValidation(){
        checkTypeValidation();
        checkReviewExisting();
    }

    private void checkTypeValidation(){ // EventType 검증
        if ( this.requestDto.getType() == null )
            throw new CustomException(NOT_FOUND_EVENT_TYPE);
    }

    private void checkReviewExisting(){ // 리뷰 중복 등록 검증
        if ( eventLogRepository.existsByPlaceIdAndUserId(
                this.requestDto.getPlaceId(),
                this.requestDto.getUserId())
        )
            throw new CustomException(ALREADY_EXISTS_REVIEW);
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
        if (this.requestDto.getContent().length() >= 1 ) { return 1; }
        else { return 0; }
    }

    private int getPointByPhotoIdsSize(){ // PhotoIdsSize에 따른 보너스 포인트 부여
        if (this.requestDto.getAttachedPhotoIds().size() >= 1) { return 1; }
        else { return 0; }
    }

    private int getPointWhenFirstReview(){ // 장소에 대한 첫 리뷰 보너스 포인트 부여

        int point = 0;

        if (eventLogRepository.existsByPlaceIdAndActionNot(
                this.requestDto.getPlaceId(), this.requestDto.getAction())
        ) point++;

        return point;
    }

    /* 포인트 총합 계산 */
    public int calculateTotalPoint(String userId){

        List<PointLog> points = eventLogRepository.findAllPointByUserId(userId);

        return points
                .stream()
                .filter(p -> !p.getAction().equals(DELETE))
                .mapToInt(PointLog::getPoint)
                .sum();
    }
}
