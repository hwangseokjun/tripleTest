package com.trple.tripletest.unit;

import com.trple.tripletest.dto.EventRequestDto;
import com.trple.tripletest.exception.CustomException;
import com.trple.tripletest.model.Event;
import com.trple.tripletest.model.Point;
import com.trple.tripletest.repository.EventRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.trple.tripletest.dto.EventActionEnum.DELETE;
import static com.trple.tripletest.exception.ErrorCode.ALREADY_EXISTS_REVIEW;
import static com.trple.tripletest.exception.ErrorCode.INVALID_ACTION_NAME;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    EventRepository eventRepository;
    private EventRequestDto requestDto;

    /* 이벤트 기록 저장 */
    public void saveEvent(EventRequestDto requestDto){
        assignEventRequestDto(requestDto);
        checkReviewExisting();
        createEventAndSave();
    }

    private void assignEventRequestDto(EventRequestDto requestDto){ // EventRequestDto 할당
        this.requestDto = requestDto;
    }

    /* 유효성 검사 */
    private void checkReviewExisting(){ // 리뷰 중복 등록 검증
        if ( eventRepository.existsByPlaceIdAndUserId(
                this.requestDto.getPlaceId(),
                this.requestDto.getUserId())
        )
            throw new CustomException(ALREADY_EXISTS_REVIEW);
    }

    private void createEventAndSave(){
        Event log = Event.createWithPoint(this.requestDto, getPoint());
        eventRepository.save(log);
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

        if (eventRepository.existsByPlaceIdAndActionNot(
                this.requestDto.getPlaceId(), this.requestDto.getAction())
        ) point++;

        return point;
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