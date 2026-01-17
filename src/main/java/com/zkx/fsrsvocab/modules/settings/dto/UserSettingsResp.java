package com.zkx.fsrsvocab.modules.settings.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class UserSettingsResp {

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
    private BigDecimal fsrsDesiredRetention;
    private Integer fsrsMaxIntervalDays;
    private Integer fsrsFuzzEnabled;

    private String learningSteps;
    private String relearningSteps;
}
