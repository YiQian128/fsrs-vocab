package com.zkx.fsrsvocab.config.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

/**
 * JWT 生成与校验
 */
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties properties;

    private SecretKey key;

    @PostConstruct
    public void init() {
        byte[] secretBytes = properties.getSecret().getBytes(StandardCharsets.UTF_8);
        if (secretBytes.length < 32) {
            throw new IllegalStateException("JWT secret 长度不足（HS256 建议 >= 32 字节），请设置 APP_JWT_SECRET");
        }
        this.key = Keys.hmacShaKeyFor(secretBytes);
    }

    public String generateAccessToken(JwtUserPrincipal principal) {
        Instant now = Instant.now();
        Instant exp = now.plus(properties.getAccessTokenTtl());

        return Jwts.builder()
                .issuer(properties.getIssuer())
                .subject(String.valueOf(principal.getUserId()))
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .claim("email", principal.getEmail())
                .claim("nickname", principal.getNickname())
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public Optional<JwtUserPrincipal> parse(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            Long userId = Long.valueOf(claims.getSubject());
            String email = claims.get("email", String.class);
            String nickname = claims.get("nickname", String.class);

            return Optional.of(new JwtUserPrincipal(userId, email, nickname));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public long accessTokenTtlSeconds() {
        return properties.getAccessTokenTtl().toSeconds();
    }
}
