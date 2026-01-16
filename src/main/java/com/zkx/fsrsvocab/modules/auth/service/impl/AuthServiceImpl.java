package com.zkx.fsrsvocab.modules.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zkx.fsrsvocab.common.api.ErrorCode;
import com.zkx.fsrsvocab.common.exception.BizException;
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

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final SysUserMapper userMapper;
    private final UserSettingsMapper settingsMapper;
    private final EmailCodeService emailCodeService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserProfileResp register(RegisterReq req) {
        emailCodeService.verifyAndConsume(req.getEmail(), EmailCodePurpose.REGISTER, req.getCode());

        Long emailCnt = userMapper.selectCount(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getEmail, req.getEmail()));
        if (emailCnt != null && emailCnt > 0) {
            throw BizException.of(ErrorCode.USER_EMAIL_EXISTS);
        }

        Long nickCnt = userMapper.selectCount(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getNickname, req.getNickname()));
        if (nickCnt != null && nickCnt > 0) {
            throw BizException.of(ErrorCode.USER_NICKNAME_EXISTS);
        }

        SysUser user = new SysUser();
        user.setEmail(req.getEmail());
        user.setNickname(req.getNickname());
        user.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        user.setStatus(1);
        userMapper.insert(user);

        // 初始化用户设置（与需求中的 group_size / voice_preference 等对齐）
        UserSettings s = new UserSettings();
        s.setUserId(user.getId());
        s.setGroupSize(20);
        s.setVoicePreference("US");
        s.setLearnMode("CARD");
        s.setListPageSize(50);
        settingsMapper.insert(s);

        return new UserProfileResp(user.getId(), user.getEmail(), user.getNickname());
    }

    @Override
    public UserProfileResp login(LoginReq req) {
        SysUser user;
        if (req.getAccount().contains("@")) {
            user = userMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                    .eq(SysUser::getEmail, req.getAccount()));
        } else {
            user = userMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                    .eq(SysUser::getNickname, req.getAccount()));
        }

        if (user == null) {
            throw BizException.of(ErrorCode.USER_NOT_FOUND);
        }
        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
            throw BizException.of(ErrorCode.PASSWORD_WRONG);
        }

        return new UserProfileResp(user.getId(), user.getEmail(), user.getNickname());
    }

    @Override
    public void resetPassword(ResetPwdReq req) {
        emailCodeService.verifyAndConsume(req.getEmail(), EmailCodePurpose.RESET_PWD, req.getCode());

        SysUser user = userMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getEmail, req.getEmail()));
        if (user == null) {
            throw BizException.of(ErrorCode.USER_NOT_FOUND);
        }

        user.setPasswordHash(passwordEncoder.encode(req.getNewPassword()));
        userMapper.updateById(user);
    }
}