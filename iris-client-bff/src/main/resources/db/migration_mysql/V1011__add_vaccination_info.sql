CREATE TABLE vaccination_infos (
	id binary(16) NOT NULL,
	external_id varchar(100) NOT NULL,
	facility_name varchar(100) NOT NULL,
	street varchar(256)  NULL,
	house_number varchar(256)  NULL,
	city varchar(256)  NULL,
	zip_code varchar(10)  NULL,
	facility_contact_first_name varchar(100) NULL,
	facility_contact_last_name varchar(100) NULL,
	facility_contact_email varchar(100) NULL,
	facility_contact_phone varchar(100) NULL, 
	created datetime NOT NULL,
	last_modified datetime NOT NULL,
	created_by binary(16) NULL,
	last_modified_by binary(16) NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (created_by) REFERENCES user_accounts(user_id),
	FOREIGN KEY (last_modified_by) REFERENCES user_accounts(user_id)
);

CREATE TABLE employees (
	id binary(16) NOT NULL,
	vaccination_info_id binary(16) NOT NULL,
	first_name varchar(100) NULL,
	last_name varchar(100) NULL,
	date_of_birth date NOT NULL,
	sex varchar(20) NOT NULL,
	email varchar(100) NULL,
	phone varchar(100) NULL,
	street varchar(256)  NULL,
	house_number varchar(256)  NULL,
	city varchar(256)  NULL,
	zip_code varchar(10)  NULL,
	vaccination varchar(50) NOT NULL,
	vaccination_status varchar(50) NOT NULL,
	PRIMARY KEY (id),
    FOREIGN KEY (vaccination_info_id) REFERENCES vaccination_infos(id)
);
