ALTER TABLE user_accounts ADD mfa_secret_enrolled Bit DEFAULT 0 NOT NULL;
ALTER TABLE user_accounts ADD mfa_secret varchar(128) NULL;