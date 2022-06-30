ALTER TABLE user_accounts ADD locked boolean DEFAULT false NOT NULL;
ALTER TABLE user_accounts ADD deleted_at DATETIME NULL;