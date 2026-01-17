package com.zkx.fsrsvocab.modules.auth.enums;

import lombok.Getter;

/**
 * 邮箱验证码用途
 */
@Getter
public enum EmailCodePurpose {

    /** 注册 */
    REGISTER,

    /** 忘记密码/重置密码 */
    RESET_PWD,

    /** 修改密码（预留） */
    CHANGE_PWD
}
