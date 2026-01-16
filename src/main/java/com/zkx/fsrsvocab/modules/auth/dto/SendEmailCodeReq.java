package com.zkx.fsrsvocab.modules.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SendEmailCodeReq {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String purpose; // REGISTER / RESET_PWD / CHANGE_PWD
}
