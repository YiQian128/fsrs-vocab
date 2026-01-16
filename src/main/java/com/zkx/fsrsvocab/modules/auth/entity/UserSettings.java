package com.zkx.fsrsvocab.modules.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user_settings")
public class UserSettings {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Integer groupSize;
    private String voicePreference; // US / UK
    private String learnMode;       // CARD / A4
    private Integer listPageSize;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
