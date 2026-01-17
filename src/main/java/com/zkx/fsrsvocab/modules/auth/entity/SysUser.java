package com.zkx.fsrsvocab.modules.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_user")
public class SysUser {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String email;

    private String nickname;

    @TableField("password_hash")
    private String passwordHash;

    /** 0/1：是否已验证邮箱（v4.2：注册后应为 true） */
    @TableField("email_verified")
    private Integer emailVerified;

    @TableField("nickname_changed_at")
    private LocalDateTime nicknameChangedAt;

    private Integer status;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
