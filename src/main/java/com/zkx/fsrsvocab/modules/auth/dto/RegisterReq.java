package com.zkx.fsrsvocab.modules.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterReq {

    @NotBlank @Email
    private String email;

    @NotBlank
    private String code; // 邮箱验证码

    @NotBlank
    @Size(min = 2, max = 20)
    private String nickname;

    @NotBlank
    @Size(min = 6, max = 30)
    private String password;
}
