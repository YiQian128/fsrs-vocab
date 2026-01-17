package com.zkx.fsrsvocab.modules.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 注册/登录返回：JWT + 用户信息
 */
@Data
@AllArgsConstructor
public class AuthTokenResp {

    /** Bearer JWT */
    private String accessToken;

    /** 固定 "Bearer" */
    private String tokenType;

    /** 过期秒数（方便前端展示/调试） */
    private long expiresIn;

    /** 用户信息 */
    private UserProfileResp profile;
}
