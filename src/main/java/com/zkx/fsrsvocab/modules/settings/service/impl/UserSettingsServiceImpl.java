package com.zkx.fsrsvocab.modules.settings.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zkx.fsrsvocab.common.api.ErrorCode;
import com.zkx.fsrsvocab.common.exception.BizException;
import com.zkx.fsrsvocab.modules.auth.entity.UserSettings;
import com.zkx.fsrsvocab.modules.auth.mapper.UserSettingsMapper;
import com.zkx.fsrsvocab.modules.settings.dto.UpdateUserSettingsReq;
import com.zkx.fsrsvocab.modules.settings.dto.UserSettingsResp;
import com.zkx.fsrsvocab.modules.settings.service.UserSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserSettingsServiceImpl implements UserSettingsService {

    private final UserSettingsMapper settingsMapper;

    @Override
    public UserSettingsResp get(Long userId) {
        UserSettings s = settingsMapper.selectOne(new LambdaQueryWrapper<UserSettings>()
                .eq(UserSettings::getUserId, userId)
                .last("LIMIT 1"));

        if (s == null) {
            // 注册时理论上会插入；这里做兜底（只写 V1 已存在字段）
            s = new UserSettings();
            s.setUserId(userId);
            s.setGroupSize(20);
            s.setVoicePreference("US");
            s.setLearnMode("CARD");
            s.setListPageSize(50);
            settingsMapper.insert(s);

            s = settingsMapper.selectOne(new LambdaQueryWrapper<UserSettings>()
                    .eq(UserSettings::getUserId, userId)
                    .last("LIMIT 1"));
        }

        return toRespWithDefault(s);
    }

    @Override
    @Transactional
    public UserSettingsResp update(Long userId, UpdateUserSettingsReq req) {
        UserSettings s = settingsMapper.selectOne(new LambdaQueryWrapper<UserSettings>()
                .eq(UserSettings::getUserId, userId)
                .last("LIMIT 1"));

        if (s == null) {
            // 兜底：先创建，再更新
            s = new UserSettings();
            s.setUserId(userId);
            s.setGroupSize(20);
            s.setVoicePreference("US");
            s.setLearnMode("CARD");
            s.setListPageSize(50);
            settingsMapper.insert(s);

            s = settingsMapper.selectOne(new LambdaQueryWrapper<UserSettings>()
                    .eq(UserSettings::getUserId, userId)
                    .last("LIMIT 1"));
        }

        // === 基础字段校验（保证不会写出离谱值）===

        if (req.getGroupSize() != null) {
            int v = req.getGroupSize();
            if (v < 1 || v > 200) {
                throw BizException.of(ErrorCode.PARAM_ERROR);
            }
            s.setGroupSize(v);
        }

        if (req.getVoicePreference() != null) {
            String v = req.getVoicePreference().trim().toUpperCase();
            if (!("US".equals(v) || "UK".equals(v))) {
                throw BizException.of(ErrorCode.PARAM_ERROR);
            }
            s.setVoicePreference(v);
        }

        if (req.getLearnMode() != null) {
            String v = req.getLearnMode().trim().toUpperCase();
            // 你项目当前阶段先允许 CARD/A4/LIST（即使数据库不强约束）
            if (!("CARD".equals(v) || "A4".equals(v) || "LIST".equals(v))) {
                throw BizException.of(ErrorCode.PARAM_ERROR);
            }
            s.setLearnMode(v);
        }

        if (req.getListPageSize() != null) {
            int v = req.getListPageSize();
            if (v < 10 || v > 200) {
                throw BizException.of(ErrorCode.PARAM_ERROR);
            }
            s.setListPageSize(v);
        }

        // === 以下是扩展字段：如果你已经做了 V2 扩展与实体字段，这里会生效；否则可保持不传、不影响 V1 ===
        // 注意：如果数据库还没扩展出这些列，但你在请求里传了这些字段，可能会在 update 时触发 SQL 列不存在错误。
        // 所以：若你当前数据库仍是 V1，只测试/只传上面四个基础字段即可。

        if (req.getListDefaultView() != null) s.setListDefaultView(req.getListDefaultView().trim().toUpperCase());
        if (req.getListHideWordDefault() != null) s.setListHideWordDefault(req.getListHideWordDefault());
        if (req.getListHideMeaningDefault() != null) s.setListHideMeaningDefault(req.getListHideMeaningDefault());
        if (req.getAllowSkipReview() != null) s.setAllowSkipReview(req.getAllowSkipReview());

        if (req.getA4DisplayMode() != null) s.setA4DisplayMode(req.getA4DisplayMode().trim().toUpperCase());
        if (req.getA4PopupSeconds() != null) s.setA4PopupSeconds(req.getA4PopupSeconds());

        if (req.getFsrsEnabled() != null) s.setFsrsEnabled(req.getFsrsEnabled());
        if (req.getFsrsDesiredRetention() != null) {
            double v = req.getFsrsDesiredRetention();
            if (v < 0.80 || v > 0.95) {
                throw BizException.of(ErrorCode.PARAM_ERROR);
            }
            s.setFsrsDesiredRetention(BigDecimal.valueOf(v));
        }
        if (req.getFsrsMaxIntervalDays() != null) s.setFsrsMaxIntervalDays(req.getFsrsMaxIntervalDays());
        if (req.getFsrsFuzzEnabled() != null) s.setFsrsFuzzEnabled(req.getFsrsFuzzEnabled());

        if (req.getLearningSteps() != null) s.setLearningSteps(req.getLearningSteps().trim());
        if (req.getRelearningSteps() != null) s.setRelearningSteps(req.getRelearningSteps().trim());

        settingsMapper.updateById(s);

        UserSettings latest = settingsMapper.selectById(s.getId());
        return toRespWithDefault(latest);
    }

    private UserSettingsResp toRespWithDefault(UserSettings s) {
        // 基础字段默认值兜底：避免出现 null
        Integer groupSize = s.getGroupSize() != null ? s.getGroupSize() : 20;
        String voicePreference = s.getVoicePreference() != null ? s.getVoicePreference() : "US";
        String learnMode = s.getLearnMode() != null ? s.getLearnMode() : "CARD";
        Integer listPageSize = s.getListPageSize() != null ? s.getListPageSize() : 50;

        // 扩展字段默认值（若 V2 未做，这些可能为 null；返回默认值更利于前端调试）
        String listDefaultView = s.getListDefaultView() != null ? s.getListDefaultView() : "ALL";
        Integer listHideWordDefault = s.getListHideWordDefault() != null ? s.getListHideWordDefault() : 0;
        Integer listHideMeaningDefault = s.getListHideMeaningDefault() != null ? s.getListHideMeaningDefault() : 0;
        Integer allowSkipReview = s.getAllowSkipReview() != null ? s.getAllowSkipReview() : 0;

        String a4DisplayMode = s.getA4DisplayMode() != null ? s.getA4DisplayMode() : "STEP";
        Integer a4PopupSeconds = s.getA4PopupSeconds() != null ? s.getA4PopupSeconds() : 3;

        Integer fsrsEnabled = s.getFsrsEnabled() != null ? s.getFsrsEnabled() : 1;
        BigDecimal fsrsDesiredRetention = s.getFsrsDesiredRetention() != null ? s.getFsrsDesiredRetention() : new BigDecimal("0.90");
        Integer fsrsMaxIntervalDays = s.getFsrsMaxIntervalDays() != null ? s.getFsrsMaxIntervalDays() : 3650;
        Integer fsrsFuzzEnabled = s.getFsrsFuzzEnabled() != null ? s.getFsrsFuzzEnabled() : 1;

        String learningSteps = s.getLearningSteps() != null ? s.getLearningSteps() : "10m,30m";
        String relearningSteps = s.getRelearningSteps() != null ? s.getRelearningSteps() : "10m";

        return new UserSettingsResp(
                groupSize, voicePreference, learnMode, listPageSize,
                listDefaultView, listHideWordDefault, listHideMeaningDefault, allowSkipReview,
                a4DisplayMode, a4PopupSeconds,
                fsrsEnabled, fsrsDesiredRetention, fsrsMaxIntervalDays, fsrsFuzzEnabled,
                learningSteps, relearningSteps
        );
    }
}
