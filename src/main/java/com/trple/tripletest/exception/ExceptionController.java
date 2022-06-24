package com.trple.tripletest.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<ResponseError> customExceptionHandler(CustomException e) {
        return ResponseError.createFrom(e.getErrorCode());
    }
}
