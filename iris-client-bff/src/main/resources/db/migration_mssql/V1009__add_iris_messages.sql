CREATE TABLE iris_message_folder (
    id binary(255) NOT NULL,
    name varchar(255) NOT NULL,
    parent_folder binary(255) NULL,
    context varchar(50) NOT NULL,
    created datetime2 NOT NULL,
    last_modified datetime2 NOT NULL,
    created_by binary(255) NULL,
    last_modified_by binary(255) NULL,
    CONSTRAINT iris_message_folder_pkey PRIMARY KEY (id),
    FOREIGN KEY (created_by) REFERENCES user_accounts(user_id),
    FOREIGN KEY (last_modified_by) REFERENCES user_accounts(user_id)
);

CREATE TABLE iris_message_folder_default (
    id binary(255) NOT NULL,
    CONSTRAINT iris_message_folder_default_pkey PRIMARY KEY (id)
);

CREATE TABLE iris_message (
    id binary(255) NOT NULL,
    folder_id binary(255) NOT NULL,
    subject varchar(500) NOT NULL,
    body varchar(6000) NOT NULL,
    hd_author_id varchar(255) NOT NULL,
    hd_author_name varchar(255) NOT NULL,
    hd_recipient_id varchar(255) NOT NULL,
    hd_recipient_name varchar(255) NOT NULL,
    is_read bit NULL,
    created datetime2 NOT NULL,
    last_modified datetime2 NOT NULL,
    created_by binary(255) NULL,
    last_modified_by binary(255) NULL,
    CONSTRAINT iris_message_pkey PRIMARY KEY (id),
    CONSTRAINT iris_message_folder_fk FOREIGN KEY (folder_id) REFERENCES iris_message_folder(id),
    FOREIGN KEY (created_by) REFERENCES user_accounts(user_id),
    FOREIGN KEY (last_modified_by) REFERENCES user_accounts(user_id)
);

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
