package com.zkx.fsrsvocab.modules.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserProfileResp {
    private Long userId;
    private String email;
    private String nickname;
}