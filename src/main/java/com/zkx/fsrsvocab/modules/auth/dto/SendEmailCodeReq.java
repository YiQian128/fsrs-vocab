package com.zkx.fsrsvocab.modules.auth.dto;

import com.zkx.fsrsvocab.modules.auth.enums.EmailCodePurpose;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SendEmailCodeReq {

    @Email(message = "email 格式不正确")
    @NotNull(message = "email 不能为空")
    private String email;

    @NotNull(message = "purpose 不能为空")
    private EmailCodePurpose purpose;
}