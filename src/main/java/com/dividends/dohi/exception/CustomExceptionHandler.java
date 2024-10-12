package com.dividends.dohi.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {

    //해당 에러를 잡으면 어떻게 보내줄지 결정
    @ExceptionHandler(AbstractException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(AbstractException e){
        ErrorResponse errorResponse = ErrorResponse.builder()
                                                    .code(e.getStatusCode())
                                                    .message(e.getMessage())
                                                    .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.resolve(e.getStatusCode()));
    }


}
