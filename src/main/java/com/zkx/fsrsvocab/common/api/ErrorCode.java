package com.zkx.fsrsvocab.common.api;

import lombok.Getter;

@Getter
public enum ErrorCode {

    OK(0, "OK"),

    // 40xxx：参数/校验错误
    PARAM_ERROR(40001, "参数错误"),

    // 41xxx：认证与账户相关
    EMAIL_CODE_INVALID(41001, "验证码无效或已过期"),
    EMAIL_CODE_RATE_LIMIT(41002, "发送过于频繁，请稍后再试"),
    USER_EMAIL_EXISTS(41003, "邮箱已被注册"),
    USER_NICKNAME_EXISTS(41004, "昵称已被占用"),
    USER_NOT_FOUND(41005, "用户不存在"),
    PASSWORD_WRONG(41006, "密码错误"),

    // ✅ 新增：JWT 登录态相关
    AUTH_UNAUTHORIZED(41007, "未登录或登录已过期"),
    AUTH_FORBIDDEN(41008, "无权限访问"),

    // 50xxx：系统错误
    SYSTEM_ERROR(50000, "系统异常");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
