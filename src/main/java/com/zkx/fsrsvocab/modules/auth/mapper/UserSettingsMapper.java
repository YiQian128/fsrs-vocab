package com.zkx.fsrsvocab.modules.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zkx.fsrsvocab.modules.auth.entity.UserSettings;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserSettingsMapper extends BaseMapper<UserSettings> {

    @Select("SELECT * FROM user_settings WHERE user_id = #{userId} LIMIT 1")
    UserSettings selectByUserId(Long userId);
}
