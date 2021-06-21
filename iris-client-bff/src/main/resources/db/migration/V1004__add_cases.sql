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

CREATE TABLE  contact (
    contact_id uuid NOT NULL,
    submission_id uuid NOT NULL,
    first_name varchar(100) NULL,
    last_name varchar(100) NULL,
    date_of_birth date NULL,
    sex varchar(20) NULL,
    email varchar(100) NULL,
    phone varchar(100) NULL,
    mobile_phone varchar(100) NULL,
    address_id uuid NULL,
    workplace_address_id uuid NULL,
    workplace_name varchar(256) NULL,
    workplace_point_of_contact varchar(256) NULL,
    first_contact_date date NULL,
    last_contact_date date NULL,
    contact_category varchar(100) NULL,
    basic_conditions varchar(256) NULL,
    PRIMARY KEY (contact_id),
    FOREIGN KEY (submission_id) REFERENCES case_data_submission(submission_id),
    FOREIGN KEY (address_id) REFERENCES address(address_id),
    FOREIGN KEY (workplace_address_id) REFERENCES address(address_id)
);

CREATE TABLE address (
    address_id uuid NOT NULL,
    street varchar(256) NULL,
    house_number varchar(256) NULL,
    zip_code varchar(10) NULL,
    city varchar(256) NULL,
    PRIMARY KEY (id)
);

