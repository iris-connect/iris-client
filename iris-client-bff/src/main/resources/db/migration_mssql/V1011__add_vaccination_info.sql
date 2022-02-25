CREATE TABLE vaccination_info (
	id binary(255) NOT NULL,
	external_id varchar(100) NOT NULL,
	announcement_token varchar(256) NULL,
	created datetime2 NOT NULL,
	last_modified datetime2 NOT NULL,
	created_by binary(255) NULL,
	last_modified_by binary(255) NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (created_by) REFERENCES user_accounts(user_id),
	FOREIGN KEY (last_modified_by) REFERENCES user_accounts(user_id)
);
