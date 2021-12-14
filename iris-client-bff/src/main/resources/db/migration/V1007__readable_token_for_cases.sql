DELETE FROM case_event;
DELETE FROM contact;
DELETE FROM case_data_submission;
DELETE FROM case_data_request;

ALTER TABLE case_data_request ADD data_authorization_token VARCHAR(255) NOT NULL;
ALTER TABLE case_data_request ADD readable_token VARCHAR(255) NOT NULL;
ALTER TABLE case_data_request DROP dw_submission_uri;