package com.zkx.fsrsvocab.modules.auth.service;

import com.zkx.fsrsvocab.modules.auth.dto.LoginReq;
import com.zkx.fsrsvocab.modules.auth.dto.RegisterReq;
import com.zkx.fsrsvocab.modules.auth.dto.ResetPwdReq;
import com.zkx.fsrsvocab.modules.auth.dto.UserProfileResp;

public interface AuthService {
    UserProfileResp register(RegisterReq req);
    UserProfileResp login(LoginReq req);
    void resetPassword(ResetPwdReq req);
}
