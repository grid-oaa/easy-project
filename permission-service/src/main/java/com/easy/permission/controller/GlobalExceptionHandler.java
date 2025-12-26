package com.easy.permission.controller;

import com.easy.common.ApiResponse;
import com.easy.common.ErrorCode;
import jakarta.validation.ConstraintViolationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 统一异常处理。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    public ApiResponse<Void> handleValidationException() {
        return ApiResponse.fail(ErrorCode.INVALID_PARAM);
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleException() {
        return ApiResponse.fail(ErrorCode.INTERNAL_ERROR);
    }
}
