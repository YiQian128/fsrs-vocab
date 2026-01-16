package com.zkx.fsrsvocab.modules.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPwdReq {

    @NotBlank @Email
    private String email;

    @NotBlank
    private String code;

    @NotBlank
    @Size(min = 6, max = 30)
    private String newPassword;
}
