package com.trple.tripletest.projection;

import com.trple.tripletest.dto.EventActionEnum;

public interface PointProjection {
    EventActionEnum getAction();
    Integer getPoint();
}
