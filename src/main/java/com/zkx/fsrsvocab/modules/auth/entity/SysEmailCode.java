package com.zkx.fsrsvocab.modules.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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

    private String codeHash;
    private String salt;

    private LocalDateTime expiresAt;
    private LocalDateTime usedAt;

    private String requestIp;
    private String requestUa;

    private LocalDateTime createdAt;
}
