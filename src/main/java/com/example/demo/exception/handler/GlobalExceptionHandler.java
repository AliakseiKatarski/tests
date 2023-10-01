package com.example.demo.exception.handler;

import com.example.demo.exception.NoSuchUserException;
import com.example.demo.exception.RuntimeExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchUserException.class)
    public ResponseEntity<RuntimeExceptionResponse> handle(NoSuchUserException e) {
        return new ResponseEntity<>(RuntimeExceptionResponse.builder()
                .date(Instant.now())
                .message(e.getMessage())
                .build(), HttpStatus.BAD_REQUEST);
    }
}
