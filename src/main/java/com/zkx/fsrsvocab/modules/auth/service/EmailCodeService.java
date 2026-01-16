package com.zkx.fsrsvocab.modules.auth.service;

import com.zkx.fsrsvocab.modules.auth.enums.EmailCodePurpose;

public interface EmailCodeService {

    /**
     * 发送验证码（当前阶段不真发邮件：只落库 + 控制台日志输出）
     */
    void sendCode(String email, EmailCodePurpose purpose, String requestIp, String requestUa);

    /** 校验验证码正确且未过期、未使用；校验成功会标记 used_at */
    void verifyAndConsume(String email, EmailCodePurpose purpose, String code);
}