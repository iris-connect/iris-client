ALTER TABLE case_data_request ADD announcement_token varchar(256) NULL;
ALTER TABLE case_data_request ADD dw_submission_uri varchar(1024) NULL;

CREATE TABLE case_data_submission (
	submission_id uuid NOT NULL,
	request_id uuid NOT NULL,
	first_name varchar(256) NULL,
	last_name varchar(256) NULL,
	date_of_birth date NULL,
	contacts_start_date timestamp with time zone NULL,
	contacts_end_date timestamp with time zone NULL,
	events_start_date timestamp with time zone NULL,
	events_end_date timestamp with time zone NULL,
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
    street varchar(256) NULL,
    house_number varchar(256) NULL,
    zip_code varchar(10) NULL,
    city varchar(256) NULL,
    workplace_street varchar(256) NULL,
    workplace_house_number varchar(256) NULL,
    workplace_zip_code varchar(10) NULL,
    workplace_city varchar(256) NULL,
    workplace_name varchar(256) NULL,
    workplace_point_of_contact varchar(256) NULL,
    first_contact_date date NULL,
    last_contact_date date NULL,
    contact_category varchar(100) NULL,
    basic_conditions varchar(256) NULL,
    PRIMARY KEY (contact_id),
    FOREIGN KEY (submission_id) REFERENCES case_data_submission(submission_id)
);

CREATE TABLE case_event (
    event_id uuid NOT NULL,
    submission_id uuid NOT NULL,
    name varchar(256) NULL,
    phone varchar(100) NULL,
    street varchar(256) NULL,
    house_number varchar(256) NULL,
    zip_code varchar(10) NULL,
    city varchar(256) NULL,
    additional_information varchar(256),
    PRIMARY KEY (event_id),
    FOREIGN KEY (submission_id) REFERENCES case_data_submission(submission_id)
);
