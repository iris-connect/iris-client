
CREATE TABLE iris_message_folder (
    id binary(16) NOT NULL,
    name varchar(255) NOT NULL,
    parent_folder binary(16) NULL,
    context varchar(50) NOT NULL,
    created datetime NOT NULL,
    last_modified datetime NOT NULL,
    CONSTRAINT iris_message_folder_pkey PRIMARY KEY (id)
);
CREATE INDEX iris_message_folder_id ON iris_message_folder (id);

CREATE TABLE iris_message_folder_default
(
    id binary(16) NOT NULL,
    CONSTRAINT iris_message_folder_default_pkey PRIMARY KEY (id)
);

CREATE TABLE iris_message (
    id binary(16) NOT NULL,
    folder_id binary(16) NOT NULL,
    subject varchar(500) NOT NULL,
    body varchar(6000) NOT NULL,
    hd_author_id varchar(255) NOT NULL,
    hd_author_name varchar(255) NOT NULL,
    hd_recipient_id varchar(255) NOT NULL,
    hd_recipient_name varchar(255) NOT NULL,
    is_read bool NULL,
    created datetime NOT NULL,
    last_modified datetime NOT NULL,
    CONSTRAINT iris_message_pkey PRIMARY KEY (id),
    CONSTRAINT iris_message_folder_fk FOREIGN KEY (folder_id) REFERENCES iris_message_folder(id)
);
CREATE INDEX iris_message_id ON iris_message (id);

CREATE TABLE iris_message_file (
    id binary(16) NOT NULL,
    message_id binary(16) NOT NULL,
    name varchar(255) NOT NULL,
    content mediumblob NULL,
    content_type varchar(50) NULL,
    created datetime NOT NULL,
    last_modified datetime NOT NULL,
    CONSTRAINT iris_message_file_pkey PRIMARY KEY (id),
    CONSTRAINT iris_message_fk FOREIGN KEY (message_id) REFERENCES iris_message(id)
);
CREATE INDEX iris_message_file_id ON iris_message_file (id);