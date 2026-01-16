package com.zkx.fsrsvocab.modules.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPwdReq {

    @Email(message = "email 格式不正确")
    @NotBlank(message = "email 不能为空")
    private String email;

    @NotBlank(message = "code 不能为空")
    private String code;

    @NotBlank(message = "newPassword 不能为空")
    @Size(min = 8, max = 72, message = "newPassword 长度 8~72")
    private String newPassword;
}