package com.trple.tripletest.controller;

import com.trple.tripletest.dto.EventRequestDto;
import com.trple.tripletest.dto.EventResponseDto;
import com.trple.tripletest.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping("/events")
    public EventResponseDto saveEvent(@RequestBody EventRequestDto requestDto){
        return eventService.saveEvent(requestDto);
    }

    @GetMapping("/events/{userId}")
    public Map<String, Integer> getTotalPoint(@PathVariable String userId){

        Map<String, Integer> totalPointMap = new HashMap<>();
        totalPointMap.put("point", eventService.calculateTotalPoint(userId));

        return totalPointMap;
    }

}
