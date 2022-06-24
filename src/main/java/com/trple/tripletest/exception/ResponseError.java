package com.trple.tripletest.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
public class ResponseError {
    private final int status;
    private final String code;
    private final String message;

    public static ResponseEntity<ResponseError> createFrom(ErrorCode errorCode) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseError.builder()
                        .status(errorCode.getHttpStatus())
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build()
                );
    }
}

