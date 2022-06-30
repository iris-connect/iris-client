ALTER TABLE user_accounts ADD mfa_secret_enrolled BOOLEAN DEFAULT FALSE NOT NULL;
ALTER TABLE user_accounts ADD mfa_secret varchar(128) NULL;