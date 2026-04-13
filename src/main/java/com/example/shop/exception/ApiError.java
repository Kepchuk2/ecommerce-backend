package com.example.shop.exception;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiError {
    LocalDateTime timestamp;
    int status;
    String error;
    String message;
    String path;

    @Singular
    List<FieldValidationError> fieldErrors;
}
