-- V1__init.sql
-- Minimal tables for Auth + Email code + User settings (group size default = 20)

CREATE TABLE IF NOT EXISTS sys_user (
                                        id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'PK',
                                        email VARCHAR(255) NOT NULL COMMENT 'User email (login identifier)',
    nickname VARCHAR(50) NOT NULL COMMENT 'Unique nickname (login identifier)',
    password_hash VARCHAR(255) NOT NULL COMMENT 'BCrypt hash',
    email_verified TINYINT(1) NOT NULL DEFAULT 0 COMMENT '0=no,1=yes',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '1=active,0=disabled',
    last_nickname_changed_at DATETIME NULL COMMENT 'Nickname change limit (1/week)',
    last_password_changed_at DATETIME NULL COMMENT 'Password change record',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at DATETIME NULL COMMENT 'Soft delete',
    PRIMARY KEY (id),
    UNIQUE KEY uk_sys_user_email (email),
    UNIQUE KEY uk_sys_user_nickname (nickname),
    KEY idx_sys_user_status (status)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Users';

CREATE TABLE IF NOT EXISTS sys_email_code (
                                              id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'PK',
                                              email VARCHAR(255) NOT NULL COMMENT 'Target email',
    purpose VARCHAR(32) NOT NULL COMMENT 'REGISTER / RESET_PWD / CHANGE_PWD',
    code_hash CHAR(64) NOT NULL COMMENT 'SHA-256 hex of (code + salt)',
    salt CHAR(16) NOT NULL COMMENT 'random salt',
    expires_at DATETIME NOT NULL COMMENT 'Expire time',
    used_at DATETIME NULL COMMENT 'Used time',
    request_ip VARCHAR(64) NULL COMMENT 'Request IP',
    request_ua VARCHAR(255) NULL COMMENT 'Request User-Agent',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_email_purpose_created (email, purpose, created_at),
    KEY idx_expires_at (expires_at)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Email verification codes';

CREATE TABLE IF NOT EXISTS user_settings (
                                             user_id BIGINT UNSIGNED NOT NULL COMMENT 'PK & FK -> sys_user.id',
                                             group_size INT NOT NULL DEFAULT 20 COMMENT 'Learn/Review group size (default 20)',
                                             voice_preference VARCHAR(8) NOT NULL DEFAULT 'US' COMMENT 'US / UK',
    list_page_size INT NOT NULL DEFAULT 50 COMMENT 'List mode page size',
    hide_word_default TINYINT(1) NOT NULL DEFAULT 0 COMMENT 'List sidebar switch default',
    hide_meaning_default TINYINT(1) NOT NULL DEFAULT 0 COMMENT 'List sidebar switch default',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id),
    CONSTRAINT fk_settings_user FOREIGN KEY (user_id) REFERENCES sys_user(id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Per-user settings';
