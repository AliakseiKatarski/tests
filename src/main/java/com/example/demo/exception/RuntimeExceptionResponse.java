package com.example.demo.exception;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class RuntimeExceptionResponse {
    private String message;
    private Instant date;
}
