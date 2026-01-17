package com.zkx.fsrsvocab.modules.settings.controller;

import com.zkx.fsrsvocab.common.api.ApiResponse;
import com.zkx.fsrsvocab.config.security.jwt.JwtUserPrincipal;
import com.zkx.fsrsvocab.modules.settings.dto.UpdateUserSettingsReq;
import com.zkx.fsrsvocab.modules.settings.dto.UserSettingsResp;
import com.zkx.fsrsvocab.modules.settings.service.UserSettingsService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 用户设置接口
 *
 * 对应 v4.2：
 * GET /api/user/settings
 * PUT /api/user/settings
 */
@RestController
@RequestMapping("/api/user/settings")
@RequiredArgsConstructor
public class UserSettingsController {

    private final UserSettingsService userSettingsService;

    @Operation(summary = "获取当前用户设置（JWT）")
    @GetMapping
    public ApiResponse<UserSettingsResp> get(@AuthenticationPrincipal JwtUserPrincipal principal) {
        return ApiResponse.ok(userSettingsService.get(principal.getUserId()));
    }

    @Operation(summary = "更新当前用户设置（JWT，支持部分更新）")
    @PutMapping
    public ApiResponse<UserSettingsResp> update(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @RequestBody @Valid UpdateUserSettingsReq req
    ) {
        return ApiResponse.ok(userSettingsService.update(principal.getUserId(), req));
    }
}
