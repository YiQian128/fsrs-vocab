package com.zkx.fsrsvocab.modules.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_email_code")
public class SysEmailCode {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String email;

    private String purpose;

    @TableField("code_hash")
    private String codeHash;

    private String salt;

    @TableField("expires_at")
    private LocalDateTime expiresAt;

    @TableField("used_at")
    private LocalDateTime usedAt;

    @TableField("request_ip")
    private String requestIp;

    @TableField("request_ua")
    private String requestUa;

    @TableField("created_at")
    private LocalDateTime createdAt;
}