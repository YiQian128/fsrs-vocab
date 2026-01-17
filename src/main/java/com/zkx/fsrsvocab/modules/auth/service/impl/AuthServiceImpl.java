package com.zkx.fsrsvocab.modules.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zkx.fsrsvocab.common.api.ErrorCode;
import com.zkx.fsrsvocab.common.exception.BizException;
import com.zkx.fsrsvocab.config.security.jwt.JwtTokenProvider;
import com.zkx.fsrsvocab.config.security.jwt.JwtUserPrincipal;
import com.zkx.fsrsvocab.modules.auth.dto.AuthTokenResp;
import com.zkx.fsrsvocab.modules.auth.dto.LoginReq;
import com.zkx.fsrsvocab.modules.auth.dto.RegisterReq;
import com.zkx.fsrsvocab.modules.auth.dto.ResetPwdReq;
import com.zkx.fsrsvocab.modules.auth.dto.UserProfileResp;
import com.zkx.fsrsvocab.modules.auth.entity.SysUser;
import com.zkx.fsrsvocab.modules.auth.entity.UserSettings;
import com.zkx.fsrsvocab.modules.auth.enums.EmailCodePurpose;
import com.zkx.fsrsvocab.modules.auth.mapper.SysUserMapper;
import com.zkx.fsrsvocab.modules.auth.mapper.UserSettingsMapper;
import com.zkx.fsrsvocab.modules.auth.service.AuthService;
import com.zkx.fsrsvocab.modules.auth.service.EmailCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final SysUserMapper userMapper;
    private final UserSettingsMapper settingsMapper;
    private final EmailCodeService emailCodeService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public AuthTokenResp register(RegisterReq req) {
        // 1) 校验并消费验证码（✅ 修复：使用接口真实方法 verifyAndConsume）
        emailCodeService.verifyAndConsume(req.getEmail(), EmailCodePurpose.REGISTER, req.getCode());

        // 2) 邮箱是否已注册
        Long emailCnt = userMapper.selectCount(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getEmail, req.getEmail()));
        if (emailCnt != null && emailCnt > 0) {
            throw BizException.of(ErrorCode.USER_EMAIL_EXISTS);
        }

        // 3) 昵称是否被占用
        Long nickCnt = userMapper.selectCount(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getNickname, req.getNickname()));
        if (nickCnt != null && nickCnt > 0) {
            throw BizException.of(ErrorCode.USER_NICKNAME_EXISTS);
        }

        // 4) 创建用户（密码用 BCrypt 哈希）
        SysUser user = new SysUser();
        user.setEmail(req.getEmail());
        user.setNickname(req.getNickname());
        user.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        user.setStatus(1);
        userMapper.insert(user);

        // 5) 初始化 user_settings（只写你当前 V1 已存在的字段；其他字段若后续扩展为 V2，可由数据库默认值接管）
        UserSettings settings = new UserSettings();
        settings.setUserId(user.getId());
        settings.setGroupSize(20);
        settings.setVoicePreference("US");
        settings.setLearnMode("CARD");
        settings.setListPageSize(50);
        settingsMapper.insert(settings);

        // 6) 生成 JWT + profile
        UserProfileResp profile = new UserProfileResp(user.getId(), user.getEmail(), user.getNickname());
        String token = jwtTokenProvider.generateAccessToken(
                new JwtUserPrincipal(user.getId(), user.getEmail(), user.getNickname())
        );

        return new AuthTokenResp(token, "Bearer", jwtTokenProvider.accessTokenTtlSeconds(), profile);
    }

    @Override
    public AuthTokenResp login(LoginReq req) {
        // account 支持：nickname 或 email
        SysUser user = userMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getNickname, req.getAccount())
                .or()
                .eq(SysUser::getEmail, req.getAccount()));

        if (user == null) {
            throw BizException.of(ErrorCode.USER_NOT_FOUND);
        }

        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
            throw BizException.of(ErrorCode.PASSWORD_WRONG);
        }

        UserProfileResp profile = new UserProfileResp(user.getId(), user.getEmail(), user.getNickname());
        String token = jwtTokenProvider.generateAccessToken(
                new JwtUserPrincipal(user.getId(), user.getEmail(), user.getNickname())
        );

        return new AuthTokenResp(token, "Bearer", jwtTokenProvider.accessTokenTtlSeconds(), profile);
    }

    @Override
    @Transactional
    public String resetPassword(ResetPwdReq req) {
        // ✅ 修复：使用 verifyAndConsume + 指定用途 RESET_PWD
        emailCodeService.verifyAndConsume(req.getEmail(), EmailCodePurpose.RESET_PWD, req.getCode());

        SysUser user = userMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getEmail, req.getEmail()));

        if (user == null) {
            throw BizException.of(ErrorCode.USER_NOT_FOUND);
        }

        user.setPasswordHash(passwordEncoder.encode(req.getNewPassword()));
        userMapper.updateById(user);

        return "OK";
    }

    @Override
    public UserProfileResp me(Long userId) {
        SysUser user = userMapper.selectById(userId);
        if (user == null) {
            throw BizException.of(ErrorCode.USER_NOT_FOUND);
        }
        return new UserProfileResp(user.getId(), user.getEmail(), user.getNickname());
    }
}
