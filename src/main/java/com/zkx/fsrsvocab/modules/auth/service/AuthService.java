package com.zkx.fsrsvocab.modules.auth.service;

import com.zkx.fsrsvocab.modules.auth.dto.AuthTokenResp;
import com.zkx.fsrsvocab.modules.auth.dto.LoginReq;
import com.zkx.fsrsvocab.modules.auth.dto.RegisterReq;
import com.zkx.fsrsvocab.modules.auth.dto.ResetPwdReq;
import com.zkx.fsrsvocab.modules.auth.dto.UserProfileResp;

public interface AuthService {

    AuthTokenResp register(RegisterReq req);

    AuthTokenResp login(LoginReq req);

    String resetPassword(ResetPwdReq req);

    UserProfileResp me(Long userId);
}
