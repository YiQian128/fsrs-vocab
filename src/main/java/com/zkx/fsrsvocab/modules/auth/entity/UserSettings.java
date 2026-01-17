package com.zkx.fsrsvocab.modules.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("user_settings")
public class UserSettings {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("group_size")
    private Integer groupSize;

    @TableField("voice_preference")
    private String voicePreference;

    @TableField("learn_mode")
    private String learnMode;

    @TableField("list_page_size")
    private Integer listPageSize;

    // === V2 扩展字段（对齐 v4.2 设置中心 / FSRS 参数）===

    @TableField("list_default_view")
    private String listDefaultView;

    @TableField("list_hide_word_default")
    private Integer listHideWordDefault;

    @TableField("list_hide_meaning_default")
    private Integer listHideMeaningDefault;

    @TableField("allow_skip_review")
    private Integer allowSkipReview;

    @TableField("a4_display_mode")
    private String a4DisplayMode;

    @TableField("a4_popup_seconds")
    private Integer a4PopupSeconds;

    @TableField("fsrs_enabled")
    private Integer fsrsEnabled;

    @TableField("fsrs_desired_retention")
    private BigDecimal fsrsDesiredRetention;

    @TableField("fsrs_max_interval_days")
    private Integer fsrsMaxIntervalDays;

    @TableField("fsrs_fuzz_enabled")
    private Integer fsrsFuzzEnabled;

    @TableField("learning_steps")
    private String learningSteps;

    @TableField("relearning_steps")
    private String relearningSteps;

    // === 通用字段 ===
    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
