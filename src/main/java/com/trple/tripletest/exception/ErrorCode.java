package com.trple.tripletest.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INVALID_ACTION_NAME(HttpStatus.BAD_REQUEST.value(), "001", "올바르지 않은 Action입니다. (ADD, MOD, DELETE만 허용)"),
    ALREADY_EXISTS_REVIEW(HttpStatus.BAD_REQUEST.value(), "002", "이미 리뷰를 등록하셨습니다."),
    NOT_FOUND_EVENT_TYPE(HttpStatus.BAD_REQUEST.value(), "003", "정확한 타입을 입력하세요. (REVIEW 허용)"),
    NOT_FOUND_UUID(HttpStatus.BAD_REQUEST.value(), "004", "UUID값이 없습니다."),
    INVALID_UUID(HttpStatus.BAD_REQUEST.value(), "005", "UUID값이 유효하지 않습니다."),
    NOT_FOUND_REVIEW(HttpStatus.BAD_REQUEST.value(), "006", "리뷰를 찾을 수 없습니다.");

    private final int httpStatus;
    private final String code;
    private final String message;
}
