package com.trple.tripletest.controller;

import com.trple.tripletest.dto.EventRequestDto;
import com.trple.tripletest.dto.EventResponseDto;
import com.trple.tripletest.model.Event;
import com.trple.tripletest.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping("/events")
    public EventResponseDto saveEvent(@RequestBody EventRequestDto requestDto){
        return eventService.saveEvent(requestDto);
    }

    @GetMapping("/events/{userId}")
    public int getTotalPoint(@PathVariable String userId){
        return eventService.calculateTotalPoint(userId);
    }

}
