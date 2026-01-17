package com.zkx.fsrsvocab.modules.system.controller;

import com.zkx.fsrsvocab.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 数据库健康检查
 */
@RestController
@RequestMapping("/api/health")
@RequiredArgsConstructor
public class DbHealthController {

    private final JdbcTemplate jdbcTemplate;

    @GetMapping("/db")
    public ApiResponse<String> db() {
        Integer one = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
        return ApiResponse.ok(one != null && one == 1 ? "OK" : "BAD");
    }
}