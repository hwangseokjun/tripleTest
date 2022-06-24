package com.trple.tripletest.controller;

import com.trple.tripletest.dto.EventRequestDto;
import com.trple.tripletest.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    // 리뷰 작성 가능, 점수 추가 필요
    @PostMapping("/events")
    public void saveEvent(@RequestBody EventRequestDto requestDto){
        eventService.saveEvent(requestDto);
    }

    // 포인트 총점 조회 or 계산
    @GetMapping("/events/{userId}")
    public int getTotalPoint(@PathVariable String userId){
        return eventService.getTotalPoint(userId);
    }

}
