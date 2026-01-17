package com.zkx.fsrsvocab.modules.settings.dto;

import lombok.Data;

/**
 * 更新用户设置（所有字段均为可选；只更新非 null 的字段）
 */
@Data
public class UpdateUserSettingsReq {

    private Integer groupSize;
    private String voicePreference;
    private String learnMode;
    private Integer listPageSize;

    private String listDefaultView;
    private Integer listHideWordDefault;
    private Integer listHideMeaningDefault;
    private Integer allowSkipReview;

    private String a4DisplayMode;
    private Integer a4PopupSeconds;

    private Integer fsrsEnabled;
    private Double fsrsDesiredRetention;
    private Integer fsrsMaxIntervalDays;
    private Integer fsrsFuzzEnabled;

    private String learningSteps;
    private String relearningSteps;
}
