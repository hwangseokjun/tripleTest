package com.trple.tripletest.repository;

import com.trple.tripletest.dto.EventActionEnum;
import com.trple.tripletest.model.Event;
import com.trple.tripletest.projection.ActionProjection;
import com.trple.tripletest.projection.PointProjection;
import com.trple.tripletest.projection.UserIdProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    Optional<ActionProjection> findFirstByPlaceIdAndUserIdAndReviewIdOrderByIdDesc(String placeId, String userId, String reviewId);

    Optional<UserIdProjection> findFirstByPlaceIdAndActionNotOrderByIdAsc(String placeId, EventActionEnum action);

    @Query( "SELECT event.point AS point, event.action AS action FROM Event event " +
            "WHERE event.userId = :userId AND event.id IN (SELECT MAX (id) FROM Event GROUP BY placeId)")
    List<PointProjection> findAllPointByUserId(@Param("userId") String userId);

    @Query( "SELECT event.action AS action FROM Event event " +
            "WHERE event.placeId = :placeId AND event.id IN (SELECT MAX (id) FROM Event GROUP BY userId)")
    List<ActionProjection> findFirstByPlaceIdGroupByUser(@Param("placeId") String placeId);

}
