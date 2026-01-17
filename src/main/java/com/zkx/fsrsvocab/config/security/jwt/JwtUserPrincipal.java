package com.zkx.fsrsvocab.config.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 放进 SecurityContext 的“当前登录用户信息”
 * 注意：这里只放最关键字段，更多信息需要时再查库
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtUserPrincipal {

    private Long userId;
    private String email;
    private String nickname;
}
