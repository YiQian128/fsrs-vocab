package com.zkx.fsrsvocab.modules.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zkx.fsrsvocab.common.api.ErrorCode;
import com.zkx.fsrsvocab.common.exception.BizException;
import com.zkx.fsrsvocab.modules.auth.dto.*;
import com.zkx.fsrsvocab.modules.auth.entity.SysUser;
import com.zkx.fsrsvocab.modules.auth.entity.UserSettings;
import com.zkx.fsrsvocab.modules.auth.enums.EmailCodePurpose;
import com.zkx.fsrsvocab.modules.auth.mapper.SysUserMapper;
import com.zkx.fsrsvocab.modules.auth.mapper.UserSettingsMapper;
import com.zkx.fsrsvocab.modules.auth.service.AuthService;
import com.zkx.fsrsvocab.modules.auth.service.EmailCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final SysUserMapper userMapper;
    private final UserSettingsMapper settingsMapper;
    private final EmailCodeService emailCodeService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    @Transactional
    public UserProfileResp register(RegisterReq req) {
        boolean ok = emailCodeService.verifyCode(req.getEmail(), EmailCodePurpose.REGISTER, req.getCode());
        if (!ok) throw new BizException(ErrorCode.BIZ_ERROR, "验证码错误或已过期");

        // email 唯一
        Long emailCnt = userMapper.selectCount(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getEmail, req.getEmail()));
        if (emailCnt != null && emailCnt > 0) {
            throw new BizException(ErrorCode.BIZ_ERROR, "该邮箱已注册");
        }

        // nickname 唯一
        Long nickCnt = userMapper.selectCount(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getNickname, req.getNickname()));
        if (nickCnt != null && nickCnt > 0) {
            throw new BizException(ErrorCode.BIZ_ERROR, "该昵称已被占用");
        }

        SysUser u = new SysUser();
        u.setEmail(req.getEmail());
        u.setNickname(req.getNickname());
        u.setPasswordHash(encoder.encode(req.getPassword()));
        u.setStatus(1);
        userMapper.insert(u);

        // 初始化设置：group_size 默认 20（按你的规划）
        UserSettings s = new UserSettings();
        s.setUserId(u.getId());
        s.setGroupSize(20);
        s.setVoicePreference("US");
        s.setLearnMode("CARD");
        s.setListPageSize(50);
        settingsMapper.insert(s);

        return toProfile(u);
    }

    @Override
    public UserProfileResp login(LoginReq req) {
        SysUser u = userMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getEmail, req.getAccount())
                .or()
                .eq(SysUser::getNickname, req.getAccount())
                .last("LIMIT 1"));

        if (u == null) throw new BizException(ErrorCode.BIZ_ERROR, "账号或密码错误");
        if (u.getStatus() != null && u.getStatus() == 0) throw new BizException(ErrorCode.BIZ_ERROR, "账号已被禁用");

        boolean ok = encoder.matches(req.getPassword(), u.getPasswordHash());
        if (!ok) throw new BizException(ErrorCode.BIZ_ERROR, "账号或密码错误");

        return toProfile(u);
    }

    @Override
    @Transactional
    public void resetPassword(ResetPwdReq req) {
        boolean ok = emailCodeService.verifyCode(req.getEmail(), EmailCodePurpose.RESET_PWD, req.getCode());
        if (!ok) throw new BizException(ErrorCode.BIZ_ERROR, "验证码错误或已过期");

        SysUser u = userMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getEmail, req.getEmail())
                .last("LIMIT 1"));
        if (u == null) throw new BizException(ErrorCode.BIZ_ERROR, "该邮箱未注册");

        u.setPasswordHash(encoder.encode(req.getNewPassword()));
        userMapper.updateById(u);
    }

    private UserProfileResp toProfile(SysUser u) {
        UserProfileResp resp = new UserProfileResp();
        resp.setUserId(u.getId());
        resp.setEmail(u.getEmail());
        resp.setNickname(u.getNickname());
        return resp;
    }
}
