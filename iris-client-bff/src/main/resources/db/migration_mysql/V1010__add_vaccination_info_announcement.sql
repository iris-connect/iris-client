CREATE TABLE vaccination_info_announcement (
	id binary(16) NOT NULL,
	external_id varchar(100) NOT NULL,
	announcement_token varchar(256) NULL,
	created datetime NOT NULL,
	last_modified datetime NOT NULL,
	created_by binary(16) NULL,
	last_modified_by binary(16) NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (created_by) REFERENCES user_accounts(user_id),
	FOREIGN KEY (last_modified_by) REFERENCES user_accounts(user_id)
);
