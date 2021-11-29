ALTER TABLE case_data_request ADD data_authorization_token VARCHAR(255) NOT NULL;
ALTER TABLE case_data_request ADD readable_token VARCHAR(255) NOT NULL;
ALTER TABLE case_data_request DROP COLUMN dw_submission_uri;