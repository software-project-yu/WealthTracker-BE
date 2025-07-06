package com.WealthTracker.demo.global.error;

import com.WealthTracker.demo.global.constants.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String message;
}
