package com.zkx.fsrsvocab.modules.auth.dto;

import lombok.Data;

@Data
public class UserProfileResp {
    private Long userId;
    private String email;
    private String nickname;
}
