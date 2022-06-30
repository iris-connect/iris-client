ALTER TABLE user_accounts ADD mfa_secret_enrolled boolean DEFAULT false NOT NULL;
ALTER TABLE user_accounts ADD mfa_secret varchar(128) NULL;