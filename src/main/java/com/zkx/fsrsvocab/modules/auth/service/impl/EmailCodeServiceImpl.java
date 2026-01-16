package com.zkx.fsrsvocab.modules.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zkx.fsrsvocab.common.api.ErrorCode;
import com.zkx.fsrsvocab.common.exception.BizException;
import com.zkx.fsrsvocab.modules.auth.entity.SysEmailCode;
import com.zkx.fsrsvocab.modules.auth.enums.EmailCodePurpose;
import com.zkx.fsrsvocab.modules.auth.mapper.SysEmailCodeMapper;
import com.zkx.fsrsvocab.modules.auth.service.EmailCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailCodeServiceImpl implements EmailCodeService {

    private final SysEmailCodeMapper emailCodeMapper;

    private static final SecureRandom RANDOM = new SecureRandom();

    // 需求默认 10 分钟有效
    private static final int EXPIRE_MINUTES = 10;

    // 最简频控：同邮箱 + 同用途 60 秒内只允许 1 次（后续可扩展同 IP、日上限等）
    private static final int MIN_INTERVAL_SECONDS = 60;

    @Override
    public void sendCode(String email, EmailCodePurpose purpose, String requestIp, String requestUa) {
        // 频控：查最近一条
        SysEmailCode last = emailCodeMapper.selectOne(new LambdaQueryWrapper<SysEmailCode>()
                .eq(SysEmailCode::getEmail, email)
                .eq(SysEmailCode::getPurpose, purpose.name())
                .orderByDesc(SysEmailCode::getCreatedAt)
                .last("LIMIT 1"));

        if (last != null && last.getCreatedAt() != null) {
            LocalDateTime cutoff = LocalDateTime.now().minusSeconds(MIN_INTERVAL_SECONDS);
            if (last.getCreatedAt().isAfter(cutoff)) {
                throw BizException.of(ErrorCode.EMAIL_CODE_RATE_LIMIT);
            }
        }

        String code = String.format("%06d", RANDOM.nextInt(1_000_000));
        String salt = randomSalt(16);
        String hash = sha256Hex(code + ":" + salt);

        SysEmailCode record = new SysEmailCode();
        record.setEmail(email);
        record.setPurpose(purpose.name());
        record.setCodeHash(hash);
        record.setSalt(salt);
        record.setExpiresAt(LocalDateTime.now().plusMinutes(EXPIRE_MINUTES));
        record.setUsedAt(null);
        record.setRequestIp(requestIp);
        record.setRequestUa(requestUa);
        record.setCreatedAt(LocalDateTime.now());

        emailCodeMapper.insert(record);

        // 开发期不真发信：仅日志输出，便于 Swagger 测试闭环
        log.info("[DEV] EmailCode purpose={}, email={}, code={} (valid {}m)", purpose, email, code, EXPIRE_MINUTES);
    }

    @Override
    public void verifyAndConsume(String email, EmailCodePurpose purpose, String code) {
        SysEmailCode record = emailCodeMapper.selectOne(new LambdaQueryWrapper<SysEmailCode>()
                .eq(SysEmailCode::getEmail, email)
                .eq(SysEmailCode::getPurpose, purpose.name())
                .isNull(SysEmailCode::getUsedAt)
                .orderByDesc(SysEmailCode::getCreatedAt)
                .last("LIMIT 1"));

        if (record == null) {
            throw BizException.of(ErrorCode.EMAIL_CODE_INVALID);
        }
        if (record.getExpiresAt() == null || record.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw BizException.of(ErrorCode.EMAIL_CODE_INVALID);
        }

        String expected = record.getCodeHash();
        String actual = sha256Hex(code + ":" + record.getSalt());
        if (!actual.equalsIgnoreCase(expected)) {
            throw BizException.of(ErrorCode.EMAIL_CODE_INVALID);
        }

        // consume
        record.setUsedAt(LocalDateTime.now());
        emailCodeMapper.updateById(record);
    }

    private static String randomSalt(int len) {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(RANDOM.nextInt(chars.length())));
        }
        return sb.toString();
    }

    private static String sha256Hex(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}