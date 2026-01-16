package com.zkx.fsrsvocab.modules.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginReq {

    @NotBlank
    private String account; // 昵称 或 邮箱

    @NotBlank
    private String password;
}
