ALTER TABLE case_data_request ADD announcement_token varchar(256) NULL;
ALTER TABLE case_data_request ADD dw_submission_uri varchar(1024) NULL;

CREATE TABLE case_data_submission (
	submission_id uuid NOT NULL,
	request_id uuid NOT NULL,
	first_name varchar(256) NULL,
	last_name varchar(256) NULL,
	date_of_birth timestamp NULL,
	created timestamp NOT NULL,
	last_modified timestamp NOT NULL,
	PRIMARY KEY (submission_id),
	FOREIGN KEY (request_id) REFERENCES case_data_request(request_id)
);

