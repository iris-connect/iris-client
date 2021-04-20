CREATE TABLE location (
    id uuid NOT NULL,
	provider_id varchar(100) NOT NULL,
	location_id varchar(100) NOT NULL,
	name varchar(500) NULL,
	contact_official_name varchar(500) NULL,
	contact_representative varchar(500) NULL,
	contact_address_street varchar(500) NULL,
	contact_address_city varchar(500) NULL,
	contact_address_zip varchar(10) NULL,
	contact_owner_email varchar(50) NULL,
	contact_email varchar(50) NULL,
	contact_phone varchar(50) NULL,

	CONSTRAINT location_pkey PRIMARY KEY (id)
);

CREATE TABLE data_request (
	request_id uuid NOT NULL,
	ref_id varchar(100) NOT NULL,
	name varchar(500) NULL,
	hd_user_id varchar(100) NULL,
	location_id uuid NULL,
	request_start timestamp NOT NULL,
	request_end timestamp NULL,
	request_details text NULL,
	status varchar(50) NOT NULL,
	created timestamp NOT NULL,
	last_modified timestamp NOT NULL,
	CONSTRAINT request_pkey PRIMARY KEY (request_id),
	CONSTRAINT data_request_location_fk FOREIGN KEY (location_id) REFERENCES location(id)
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

CREATE TABLE data_submission (
	submission_id uuid NOT NULL,
	request_id uuid NOT NULL,
	name varchar(256) NULL,
	additional_information varchar(500) NULL,
	street varchar(256) NULL,
	house_number varchar(256) NULL,
	city varchar(256) NULL,
	zip_code varchar(10) NULL,
	start_date timestamp with time zone NULL,
	end_date timestamp with time zone NULL,
	created timestamp NOT NULL,
	last_modified timestamp NOT NULL,
	PRIMARY KEY (submission_id),
	FOREIGN KEY (request_id) REFERENCES data_request(request_id)
);

CREATE TABLE guest (
	guest_id uuid NOT NULL,
	submission_id uuid NOT NULL,
	first_name varchar(100) NULL,
	last_name varchar(100) NULL,
	date_of_birth date NULL,
	sex varchar(20) NULL,
	email varchar(100) NULL,
	phone varchar(100) NULL,
	mobile_phone varchar(100) NULL,
	identity_checked boolean NULL,
	street varchar(256)  NULL,
	house_number varchar(256)  NULL,
	city varchar(256)  NULL,
	zip_code varchar(10)  NULL,
	description_of_participation varchar(500) NULL,
	attend_from timestamp with time zone NULL,
	attend_to timestamp with time zone NULL,
	additional_information varchar(500) NULL,
	PRIMARY KEY (guest_id),
    FOREIGN KEY (submission_id) REFERENCES data_submission(submission_id)
);

CREATE TABLE sync_times (
    data_type varchar(50) NOT NULL,
    last_sync timestamp NOT NULL,
    CONSTRAINT sync_times_pkey PRIMARY KEY (data_type)
);

CREATE TABLE user_accounts (
    user_id uuid NOT NULL,
    user_name varchar(50) NOT NULL,
    password varchar(200) NOT NULL,
    PRIMARY KEY (user_id),
    CONSTRAINT user_name_unique UNIQUE(user_name)
);

CREATE INDEX user_accounts_user_name ON user_accounts (user_name);
