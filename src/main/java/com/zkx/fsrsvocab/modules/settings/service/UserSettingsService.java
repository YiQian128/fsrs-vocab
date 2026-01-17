package com.zkx.fsrsvocab.modules.settings.service;

import com.zkx.fsrsvocab.modules.settings.dto.UpdateUserSettingsReq;
import com.zkx.fsrsvocab.modules.settings.dto.UserSettingsResp;

public interface UserSettingsService {

    UserSettingsResp get(Long userId);

    UserSettingsResp update(Long userId, UpdateUserSettingsReq req);
}
