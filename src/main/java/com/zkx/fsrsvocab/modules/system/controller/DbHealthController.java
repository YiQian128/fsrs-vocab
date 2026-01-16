package com.zkx.fsrsvocab.modules.system.controller;

import com.zkx.fsrsvocab.common.api.ApiResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DbHealthController {

    private final JdbcTemplate jdbcTemplate;

    public DbHealthController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/api/health/db")
    public ApiResponse<String> dbHealth() {
        Integer v = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
        return ApiResponse.ok(v != null && v == 1 ? "OK" : "FAIL");
    }
}
