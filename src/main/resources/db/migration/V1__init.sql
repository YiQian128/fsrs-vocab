-- V1__init.sql

CREATE TABLE sys_user (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          email VARCHAR(255) NOT NULL,
                          nickname VARCHAR(50) NOT NULL,
                          password_hash VARCHAR(255) NOT NULL,

                          nickname_changed_at DATETIME NULL,

                          status TINYINT NOT NULL DEFAULT 1,
                          created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                          UNIQUE KEY uk_user_email (email),
                          UNIQUE KEY uk_user_nickname (nickname)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE user_settings (
                               id BIGINT PRIMARY KEY AUTO_INCREMENT,
                               user_id BIGINT NOT NULL,

                               group_size INT NOT NULL DEFAULT 20,
                               voice_preference VARCHAR(10) NOT NULL DEFAULT 'US',
                               learn_mode VARCHAR(10) NOT NULL DEFAULT 'CARD',
                               list_page_size INT NOT NULL DEFAULT 50,

                               created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                               UNIQUE KEY uk_settings_user (user_id),
                               CONSTRAINT fk_settings_user FOREIGN KEY (user_id) REFERENCES sys_user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE sys_email_code (
                                id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                email VARCHAR(255) NOT NULL,
                                purpose VARCHAR(20) NOT NULL,

                                code_hash VARCHAR(255) NOT NULL,
                                salt VARCHAR(50) NOT NULL,

                                expires_at DATETIME NOT NULL,
                                used_at DATETIME NULL,

                                request_ip VARCHAR(64) NULL,
                                request_ua VARCHAR(255) NULL,

                                created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                INDEX idx_email_purpose_created (email, purpose, created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
