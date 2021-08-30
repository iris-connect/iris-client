CREATE TABLE login_attempts (
	reference varchar(32) NOT NULL,
	attempts int NOT NULL,
	next_warning_threshold int NOT NULL,
	waiting_time bigint NOT NULL,
	created datetime2 NOT NULL,
	last_modified datetime2 NOT NULL,
	PRIMARY KEY (reference)
);
