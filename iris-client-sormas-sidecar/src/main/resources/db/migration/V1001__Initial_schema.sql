CREATE TABLE data_request (
	request_id uuid NOT NULL,
	ref_id varchar(50) NOT NULL,
	person_id varchar(50) NULL,
	iris_user_id varchar(50) NOT NULL,
	sormas_user_id varchar(50) NOT NULL,
	tele_code varchar(10) NOT NULL,
	check_code_one varchar(50) NULL,
	check_code_two varchar(50) NULL,
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

CREATE INDEX data_request_ref_id ON data_request (ref_id);
CREATE INDEX data_request_tele_code ON data_request (tele_code);

CREATE TABLE sync_times (
    data_type varchar(50) NOT NULL,    
    last_sync timestamp NOT NULL,
    CONSTRAINT sync_times_pkey PRIMARY KEY (data_type)
);
