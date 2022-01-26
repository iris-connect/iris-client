ALTER TABLE case_data_request ADD created_by varchar(256) NULL;
ALTER TABLE case_data_request ADD last_modified_by varchar(256) NULL;

ALTER TABLE case_data_submission ADD created_by varchar(256) NULL;
ALTER TABLE case_data_submission ADD last_modified_by varchar(256) NULL;

ALTER TABLE event_data_request ADD created_by varchar(256) NULL;
ALTER TABLE event_data_request ADD last_modified_by varchar(256) NULL;

ALTER TABLE event_data_submission ADD created_by varchar(256) NULL;
ALTER TABLE event_data_submission ADD last_modified_by varchar(256) NULL;

ALTER TABLE user_accounts ADD created datetime2 NULL;
ALTER TABLE user_accounts ADD created_by varchar(256) NULL;
ALTER TABLE user_accounts ADD last_modified datetime2 NULL;
ALTER TABLE user_accounts ADD last_modified_by varchar(256) NULL;
	