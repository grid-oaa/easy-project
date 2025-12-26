package com.easy.auth.controller;

import com.easy.auth.service.AuthService;
import com.easy.common.ApiResponse;
import com.easy.common.ErrorCode;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 统一异常处理。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthService.AuthServiceException.class)
    public ApiResponse<Void> handleAuthException(AuthService.AuthServiceException ex) {
        return ApiResponse.fail(ex.getErrorCode());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    public ResponseEntity<ApiResponse<Void>> handleValidationException(Exception ex) {
        String message = ErrorCode.INVALID_PARAM.message();
        if (ex instanceof MethodArgumentNotValidException methodEx) {
            FieldError fieldError = methodEx.getBindingResult().getFieldError();
            // 优先返回第一个字段校验提示，便于前端直接展示
            if (fieldError != null && fieldError.getDefaultMessage() != null) {
                message = fieldError.getDefaultMessage();
            }
        } else if (ex instanceof ConstraintViolationException constraintEx) {
            for (ConstraintViolation<?> violation : constraintEx.getConstraintViolations()) {
                if (violation.getMessage() != null && !violation.getMessage().isBlank()) {
                    message = violation.getMessage();
                    break;
                }
            }
        }
        return ResponseEntity.ok(ApiResponse.fail(ErrorCode.INVALID_PARAM, message));
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleException() {
        return ApiResponse.fail(ErrorCode.INTERNAL_ERROR);
    }
}
