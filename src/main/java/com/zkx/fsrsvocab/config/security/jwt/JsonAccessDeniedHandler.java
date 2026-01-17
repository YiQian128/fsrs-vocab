package com.zkx.fsrsvocab.config.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zkx.fsrsvocab.common.api.ApiResponse;
import com.zkx.fsrsvocab.common.api.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 已登录但无权限时的统一 JSON 返回（403）
 */
@Component
@RequiredArgsConstructor
public class JsonAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApiResponse<Object> body = ApiResponse.fail(ErrorCode.AUTH_FORBIDDEN);
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}
