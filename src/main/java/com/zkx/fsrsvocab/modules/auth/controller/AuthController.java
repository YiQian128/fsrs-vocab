package com.zkx.fsrsvocab.modules.auth.controller;

import com.zkx.fsrsvocab.common.api.ApiResponse;
import com.zkx.fsrsvocab.common.api.ErrorCode;
import com.zkx.fsrsvocab.common.exception.BizException;
import com.zkx.fsrsvocab.modules.auth.dto.*;
import com.zkx.fsrsvocab.modules.auth.enums.EmailCodePurpose;
import com.zkx.fsrsvocab.modules.auth.service.AuthService;
import com.zkx.fsrsvocab.modules.auth.service.EmailCodeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final EmailCodeService emailCodeService;

    @PostMapping("/email-code")
    public ApiResponse<String> sendEmailCode(@Valid @RequestBody SendEmailCodeReq req,
                                             HttpServletRequest request) {
        EmailCodePurpose purpose;
        try {
            purpose = EmailCodePurpose.valueOf(req.getPurpose());
        } catch (Exception e) {
            throw new BizException(ErrorCode.PARAM_INVALID, "purpose 不合法");
        }

        String ip = request.getRemoteAddr();
        String ua = request.getHeader("User-Agent");
        emailCodeService.sendCode(req.getEmail(), purpose, ip, ua);
        return ApiResponse.ok("SENT");
    }

    // 在类里追加字段
    private final AuthService authService;

    // 追加接口
    @PostMapping("/register")
    public ApiResponse<UserProfileResp> register(@Valid @RequestBody RegisterReq req) {
        return ApiResponse.ok(authService.register(req));
    }

    @PostMapping("/login")
    public ApiResponse<UserProfileResp> login(@Valid @RequestBody LoginReq req) {
        return ApiResponse.ok(authService.login(req));
    }

    @PostMapping("/password/reset")
    public ApiResponse<String> resetPassword(@Valid @RequestBody ResetPwdReq req) {
        authService.resetPassword(req);
        return ApiResponse.ok("OK");
    }
}
