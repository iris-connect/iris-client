CREATE TABLE iris_message_file (
    id binary(16) NOT NULL,
    message_id binary(16) NOT NULL,
    name varchar(255) NOT NULL,
    content mediumblob NULL,
    created datetime NOT NULL,
    last_modified datetime NOT NULL,
    created_by binary(16) NULL,
    last_modified_by binary(16) NULL,
    CONSTRAINT iris_message_file_pkey PRIMARY KEY (id),
    CONSTRAINT iris_message_file_message_fk FOREIGN KEY (message_id) REFERENCES iris_message(id),
    FOREIGN KEY (created_by) REFERENCES user_accounts(user_id),
    FOREIGN KEY (last_modified_by) REFERENCES user_accounts(user_id)
);
