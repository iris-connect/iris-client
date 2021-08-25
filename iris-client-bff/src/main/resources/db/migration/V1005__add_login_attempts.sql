CREATE TABLE login_attempts (
	remote_addr varchar(32) NOT NULL,
	attempts int NOT NULL,
	next_warning_threshold int NOT NULL,
	waiting_time bigint NOT NULL,
	created timestamp NOT NULL,
	last_modified timestamp NOT NULL,
	PRIMARY KEY (remote_addr)
);
