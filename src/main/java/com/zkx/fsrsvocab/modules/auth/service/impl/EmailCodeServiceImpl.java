package com.zkx.fsrsvocab.modules.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zkx.fsrsvocab.common.api.ErrorCode;
import com.zkx.fsrsvocab.common.exception.BizException;
import com.zkx.fsrsvocab.modules.auth.entity.SysEmailCode;
import com.zkx.fsrsvocab.modules.auth.enums.EmailCodePurpose;
import com.zkx.fsrsvocab.modules.auth.mapper.SysEmailCodeMapper;
import com.zkx.fsrsvocab.modules.auth.service.EmailCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailCodeServiceImpl implements EmailCodeService {

    private final SysEmailCodeMapper emailCodeMapper;

    private static final int CODE_LEN = 6;
    private static final int EXPIRE_MINUTES = 10;

    @Override
    public void sendCode(String email, EmailCodePurpose purpose, String requestIp, String requestUa) {
        String code = generate6Digits();
        String salt = randomSalt16();
        String hash = sha256Hex(code + salt);

        SysEmailCode row = new SysEmailCode();
        row.setEmail(email);
        row.setPurpose(purpose.name());
        row.setCodeHash(hash);
        row.setSalt(salt);
        row.setExpiresAt(LocalDateTime.now().plusMinutes(EXPIRE_MINUTES));
        row.setUsedAt(null);
        row.setRequestIp(requestIp);
        row.setRequestUa(requestUa);
        row.setCreatedAt(LocalDateTime.now());

        emailCodeMapper.insert(row);

        // 开发阶段：先打印到日志。后续接 SMTP 再真正发邮件
        System.out.println("[DEV] EmailCode purpose=" + purpose + ", email=" + email + ", code=" + code + " (valid " + EXPIRE_MINUTES + "m)");
    }

    @Override
    public boolean verifyCode(String email, EmailCodePurpose purpose, String code) {
        SysEmailCode latest = emailCodeMapper.selectOne(
                new LambdaQueryWrapper<SysEmailCode>()
                        .eq(SysEmailCode::getEmail, email)
                        .eq(SysEmailCode::getPurpose, purpose.name())
                        .orderByDesc(SysEmailCode::getId)
                        .last("LIMIT 1")
        );

        if (latest == null) {
            return false;
        }
        if (latest.getUsedAt() != null) {
            return false;
        }
        if (latest.getExpiresAt().isBefore(LocalDateTime.now())) {
            return false;
        }

        String calc = sha256Hex(code + latest.getSalt());
        boolean ok = calc.equalsIgnoreCase(latest.getCodeHash());
        if (ok) {
            latest.setUsedAt(LocalDateTime.now());
            emailCodeMapper.updateById(latest);
        }
        return ok;
    }

    private String generate6Digits() {
        int v = new Random().nextInt(900000) + 100000;
        return String.valueOf(v);
    }

    private String randomSalt16() {
        String chars = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random r = new Random();
        StringBuilder sb = new StringBuilder(16);
        for (int i = 0; i < 16; i++) sb.append(chars.charAt(r.nextInt(chars.length())));
        return sb.toString();
    }

    private String sha256Hex(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] dig = md.digest(s.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(dig);
        } catch (Exception e) {
            throw new BizException(ErrorCode.INTERNAL_ERROR, "验证码哈希失败");
        }
    }
}
