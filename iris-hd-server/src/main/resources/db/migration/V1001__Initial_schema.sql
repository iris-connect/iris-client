CREATE TABLE data_request (
	request_id uuid NOT NULL,
	department_id uuid NOT NULL,
	ref_id varchar(50) NOT NULL,
	iris_user_id varchar(50) NOT NULL,
	sormas_user_id varchar(50) NOT NULL,
	check_code_name varchar(50) NULL,
	check_code_day_of_birth varchar(50) NULL,
	check_code_random varchar(50) NULL,
	request_start timestamp NULL,
	request_end timestamp NULL,
	status varchar(50) NOT NULL,
	created timestamp NOT NULL,
	last_modified timestamp NOT NULL,
	CONSTRAINT request_pkey PRIMARY KEY (request_id)
);

CREATE TABLE data_request_feature (
    request_id uuid,
	feature varchar(50),
    PRIMARY KEY (request_id, feature),
    FOREIGN KEY (request_id) REFERENCES data_request(request_id)
    	ON DELETE CASCADE
    	ON UPDATE CASCADE
);

CREATE TABLE data_submission (
	submission_id uuid NOT NULL,
	submission_type varchar(31),
	request_id uuid NOT NULL,
	department_id uuid NOT NULL,
	salt varchar(50) NOT NULL,
	key_referenz varchar(50) NOT NULL,
	feature varchar(50) NOT NULL,
	encrypted_data text NULL,
	created timestamp NOT NULL,
	last_modified timestamp NOT NULL,
	CONSTRAINT submission_pkey PRIMARY KEY (submission_id)
);

CREATE TABLE sync_times (
    data_type varchar(50) NOT NULL,    
    last_sync timestamp NOT NULL,
    CONSTRAINT sync_times_pkey PRIMARY KEY (data_type)
);

--CREATE INDEX data_request_ref_id ON data_request (ref_id);
CREATE INDEX data_submission_department_id ON data_submission (department_id);
CREATE INDEX data_submission_request_id ON data_submission (request_id);
