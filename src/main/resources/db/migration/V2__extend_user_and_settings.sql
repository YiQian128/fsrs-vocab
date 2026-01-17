-- V2__extend_user_and_settings.sql
-- 目标：对齐 v4.2 用户系统（M2）所需的部分字段：email_verified + 设置中心扩展字段

ALTER TABLE sys_user
    ADD COLUMN email_verified TINYINT NOT NULL DEFAULT 0 AFTER password_hash;

ALTER TABLE user_settings
    ADD COLUMN list_default_view VARCHAR(8) NOT NULL DEFAULT 'ALL' AFTER list_page_size,
    ADD COLUMN list_hide_word_default TINYINT NOT NULL DEFAULT 0 AFTER list_default_view,
    ADD COLUMN list_hide_meaning_default TINYINT NOT NULL DEFAULT 0 AFTER list_hide_word_default,
    ADD COLUMN allow_skip_review TINYINT NOT NULL DEFAULT 0 AFTER list_hide_meaning_default,
    ADD COLUMN a4_display_mode VARCHAR(16) NOT NULL DEFAULT 'STEP' AFTER allow_skip_review,
    ADD COLUMN a4_popup_seconds INT NOT NULL DEFAULT 3 AFTER a4_display_mode,
    ADD COLUMN fsrs_enabled TINYINT NOT NULL DEFAULT 1 AFTER a4_popup_seconds,
    ADD COLUMN fsrs_desired_retention DECIMAL(4,2) NOT NULL DEFAULT 0.90 AFTER fsrs_enabled,
    ADD COLUMN fsrs_max_interval_days INT NOT NULL DEFAULT 3650 AFTER fsrs_desired_retention,
    ADD COLUMN fsrs_fuzz_enabled TINYINT NOT NULL DEFAULT 1 AFTER fsrs_max_interval_days,
    ADD COLUMN learning_steps VARCHAR(64) NOT NULL DEFAULT '10m,30m' AFTER fsrs_fuzz_enabled,
    ADD COLUMN relearning_steps VARCHAR(64) NOT NULL DEFAULT '10m' AFTER learning_steps;
