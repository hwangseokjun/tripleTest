package com.trple.tripletest.repository;

import com.trple.tripletest.dto.EventActionEnum;
import com.trple.tripletest.model.EventLog;
import com.trple.tripletest.model.PointLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventLogRepository extends JpaRepository<EventLog, Long> {

    Boolean existsByPlaceIdAndActionNot(String placeId, EventActionEnum action);

    Boolean existsByPlaceIdAndUserId(String placeId, String UserId);

    @Query( "SELECT log.point AS point, log.action AS action FROM EventLog log " +
            "WHERE log.id IN (SELECT MAX (id) FROM EventLog GROUP BY :userId)")
    List<PointLog> findAllPointByUserId(@Param("userId") String userId);

}
