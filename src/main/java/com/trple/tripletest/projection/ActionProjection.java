package com.trple.tripletest.projection;

import com.trple.tripletest.dto.EventActionEnum;

public interface ActionProjection {
    EventActionEnum getAction();
}
