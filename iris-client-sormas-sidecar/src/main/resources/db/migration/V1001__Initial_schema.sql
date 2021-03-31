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
	name varchar(500) NOT NULL,
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

CREATE TABLE sync_times (
    data_type varchar(50) NOT NULL,    
    last_sync timestamp NOT NULL,
    CONSTRAINT sync_times_pkey PRIMARY KEY (data_type)
);
