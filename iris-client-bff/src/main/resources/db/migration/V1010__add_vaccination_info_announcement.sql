CREATE TABLE vaccination_info_announcement (
	id uuid NOT NULL,
	external_id varchar(100) NOT NULL,
	announcement_token varchar(256) NULL,
	created timestamp NOT NULL,
	last_modified timestamp NOT NULL,
	created_by uuid NULL,
	last_modified_by uuid NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (created_by) REFERENCES user_accounts(user_id),
	FOREIGN KEY (last_modified_by) REFERENCES user_accounts(user_id)
);
