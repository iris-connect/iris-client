
CREATE TABLE iris_message_file (
    id binary(255) NOT NULL,
    message_id binary(255) NOT NULL,
    name varchar(255) NOT NULL,
    content varbinary(max) NULL,
    created datetime2 NOT NULL,
    last_modified datetime2 NOT NULL,
    created_by binary(255) NULL,
    last_modified_by binary(255) NULL,
    CONSTRAINT iris_message_file_pkey PRIMARY KEY (id),
    CONSTRAINT iris_message_file_message_fk FOREIGN KEY (message_id) REFERENCES iris_message(id),
    FOREIGN KEY (created_by) REFERENCES user_accounts(user_id),
    FOREIGN KEY (last_modified_by) REFERENCES user_accounts(user_id)
);
