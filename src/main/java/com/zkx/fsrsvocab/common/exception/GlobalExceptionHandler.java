package com.zkx.fsrsvocab.common.exception;

import com.zkx.fsrsvocab.common.api.ApiResponse;
import com.zkx.fsrsvocab.common.api.ErrorCode;
import jakarta.validation.ConstraintViolationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BizException.class)
    public ApiResponse<Void> handleBiz(BizException e) {
        return ApiResponse.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class, ConstraintViolationException.class})
    public ApiResponse<Void> handleValidation(Exception e) {
        return ApiResponse.fail(ErrorCode.PARAM_INVALID, "参数不合法");
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleAny(Exception e) {
        return ApiResponse.fail(ErrorCode.INTERNAL_ERROR, "服务器内部错误");
    }
}
