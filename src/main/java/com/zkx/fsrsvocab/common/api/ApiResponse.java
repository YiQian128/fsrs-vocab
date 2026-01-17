package com.zkx.fsrsvocab.common.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 统一接口返回结构
 *
 * code: 业务码（0 表示成功，非 0 表示失败）
 * message: 提示信息
 * data: 业务数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    /** 0 表示成功，其余为业务/系统错误 */
    private int code;

    /** 人类可读提示 */
    private String message;

    /** 成功时的业务数据 */
    private T data;

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), data);
    }

    public static <T> ApiResponse<T> ok() {
        return ok(null);
    }

    public static <T> ApiResponse<T> fail(ErrorCode errorCode) {
        return new ApiResponse<>(errorCode.getCode(), errorCode.getMessage(), null);
    }

    public static <T> ApiResponse<T> fail(ErrorCode errorCode, String overrideMessage) {
        return new ApiResponse<>(errorCode.getCode(), overrideMessage, null);
    }
}