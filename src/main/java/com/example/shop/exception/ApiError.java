package com.example.shop.exception;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiError {
    LocalDateTime timestamp;
    int status;
    String error;
    String message;
    String path;
}
