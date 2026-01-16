package com.zkx.fsrsvocab.modules.auth.controller;

import com.zkx.fsrsvocab.common.api.ApiResponse;
import com.zkx.fsrsvocab.modules.auth.dto.LoginReq;
import com.zkx.fsrsvocab.modules.auth.dto.RegisterReq;
import com.zkx.fsrsvocab.modules.auth.dto.ResetPwdReq;
import com.zkx.fsrsvocab.modules.auth.dto.SendEmailCodeReq;
import com.zkx.fsrsvocab.modules.auth.dto.UserProfileResp;
import com.zkx.fsrsvocab.modules.auth.service.AuthService;
import com.zkx.fsrsvocab.modules.auth.service.EmailCodeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final EmailCodeService emailCodeService;
    private final AuthService authService;

    @PostMapping("/email-code")
    public ApiResponse<String> sendEmailCode(@Valid @RequestBody SendEmailCodeReq req, HttpServletRequest request) {
        emailCodeService.sendCode(req.getEmail(), req.getPurpose(), clientIp(request), request.getHeader("User-Agent"));
        return ApiResponse.ok("SENT");
    }

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

    private String clientIp(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) {
            return xff.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}