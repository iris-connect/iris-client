ALTER TABLE user_accounts ADD locked bit DEFAULT 0 NOT NULL;
ALTER TABLE user_accounts ADD deleted_at datetime2 NULL;