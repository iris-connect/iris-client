ALTER TABLE data_request ADD comment varchar(500) NULL;
ALTER TABLE data_request ADD data_request_type varchar(31);
ALTER TABLE data_request ALTER COLUMN request_details TYPE varchar(500);
