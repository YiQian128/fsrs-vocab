package com.zkx.fsrsvocab.config.openapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI / Swagger 配置
 *
 * 作用：
 * 1) 在 Swagger UI 顶部显示 Authorize 按钮
 * 2) 让 Swagger UI 能把 JWT 放进请求头：
 *    Authorization: Bearer <token>
 *
 * 注意：
 * - 这只是“接口文档的安全声明”，不会改变你后端真实的鉴权逻辑
 * - 即便某些接口不需要登录，带着 token 访问也不会有副作用
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "FSRS-Vocab API",
                version = "v1",
                description = "FSRS-Vocab 后端接口文档"
        ),
        // 让 Swagger 默认认为需要 JWT（从而显示 Authorize 并自动加 Authorization header）
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
