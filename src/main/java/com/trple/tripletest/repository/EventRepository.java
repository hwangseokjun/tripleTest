package com.trple.tripletest.repository;

import com.trple.tripletest.dto.EventActionEnum;
import com.trple.tripletest.model.Event;
import com.trple.tripletest.model.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    Boolean existsByPlaceIdAndActionNot(String placeId, EventActionEnum action); // 잘못되었음. 수정 필요.

    Boolean existsByPlaceIdAndUserId(String placeId, String UserId);

    @Query( "SELECT event.point AS point, event.action AS action FROM Event event " +
            "WHERE event.id IN (SELECT MAX (id) FROM Event GROUP BY :userId)")
    List<Point> findAllPointByUserId(@Param("userId") String userId);

}
