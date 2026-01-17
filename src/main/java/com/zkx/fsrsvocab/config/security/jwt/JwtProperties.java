package com.zkx.fsrsvocab.config.security.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * JWT 配置项（读取 application-dev.yml 的 app.jwt.*）
 */
@Data
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {

    /** JWT issuer */
    private String issuer = "fsrs-vocab";

    /**
     * HS256 secret，长度建议 >= 32
     * 生产环境务必用环境变量注入，不要写死在仓库里
     */
    private String secret = "PLEASE_CHANGE_ME_AT_LEAST_32_CHARS_LONG";

    /** access token TTL（例如 7d） */
    private Duration accessTokenTtl = Duration.ofDays(7);
}
