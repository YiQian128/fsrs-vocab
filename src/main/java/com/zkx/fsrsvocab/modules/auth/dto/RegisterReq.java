package com.zkx.fsrsvocab.modules.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterReq {

    @Email(message = "email 格式不正确")
    @NotBlank(message = "email 不能为空")
    private String email;

    @NotBlank(message = "code 不能为空")
    @Size(min = 4, max = 10, message = "code 长度不正确")
    private String code;

    @NotBlank(message = "nickname 不能为空")
    @Size(min = 2, max = 50, message = "nickname 长度 2~50")
    private String nickname;

    @NotBlank(message = "password 不能为空")
    @Size(min = 8, max = 72, message = "password 长度 8~72")
    private String password;
}