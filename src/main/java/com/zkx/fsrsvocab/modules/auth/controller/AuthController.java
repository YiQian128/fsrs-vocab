package com.zkx.fsrsvocab.modules.auth.controller;

import com.zkx.fsrsvocab.common.api.ApiResponse;
import com.zkx.fsrsvocab.config.security.jwt.JwtUserPrincipal;
import com.zkx.fsrsvocab.modules.auth.dto.*;
import com.zkx.fsrsvocab.modules.auth.service.AuthService;
import com.zkx.fsrsvocab.modules.auth.service.EmailCodeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 认证模块 Controller
 *
 * 对应 v4.2：
 * POST /api/auth/email/code
 * POST /api/auth/register
 * POST /api/auth/login
 * POST /api/auth/password/reset
 * GET  /api/auth/me
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final EmailCodeService emailCodeService;
    private final AuthService authService;

    @Operation(summary = "发送邮箱验证码（REGISTER/RESET_PWD/CHANGE_PWD）")
    @PostMapping("/email/code")
    public ApiResponse<String> sendEmailCode(
            @RequestBody @Valid SendEmailCodeReq req,
            HttpServletRequest request
    ) {
        String ip = clientIp(request);
        String ua = request.getHeader("User-Agent");
        emailCodeService.sendCode(req.getEmail(), req.getPurpose(), ip, ua);
        return ApiResponse.ok("SENT");
    }

    @Operation(summary = "注册（返回 JWT + profile）")
    @PostMapping("/register")
    public ApiResponse<AuthTokenResp> register(@RequestBody @Valid RegisterReq req) {
        return ApiResponse.ok(authService.register(req));
    }

    @Operation(summary = "登录（返回 JWT + profile）")
    @PostMapping("/login")
    public ApiResponse<AuthTokenResp> login(@RequestBody @Valid LoginReq req) {
        return ApiResponse.ok(authService.login(req));
    }

    @Operation(summary = "忘记密码/重置密码")
    @PostMapping("/password/reset")
    public ApiResponse<String> resetPassword(@RequestBody @Valid ResetPwdReq req) {
        return ApiResponse.ok(authService.resetPassword(req));
    }

    @Operation(summary = "获取当前登录用户信息（JWT）")
    @GetMapping("/me")
    public ApiResponse<UserProfileResp> me(@AuthenticationPrincipal JwtUserPrincipal principal) {
        return ApiResponse.ok(authService.me(principal.getUserId()));
    }

    @Operation(summary = "退出登录（前端删除 token 即可，后端预留）")
    @PostMapping("/logout")
    public ApiResponse<String> logout() {
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
