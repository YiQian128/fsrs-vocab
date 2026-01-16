package com.zkx.fsrsvocab.modules.auth.service;

import com.zkx.fsrsvocab.modules.auth.enums.EmailCodePurpose;

public interface EmailCodeService {
    void sendCode(String email, EmailCodePurpose purpose, String requestIp, String requestUa);
    boolean verifyCode(String email, EmailCodePurpose purpose, String code);
}
