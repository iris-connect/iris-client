
CREATE TABLE  iris_message_folder (
    id uuid NOT NULL,
    name varchar(255) NOT NULL,
    parent_folder uuid NULL,
    context varchar(50) NOT NULL,
    created timestamp NOT NULL,
    last_modified timestamp NOT NULL,
    CONSTRAINT iris_message_folder_pkey PRIMARY KEY (id)
);
CREATE INDEX iris_message_folder_id ON iris_message_folder (id);

CREATE TABLE  iris_message_folder_default
(
    id uuid NOT NULL,
    CONSTRAINT iris_message_folder_default_pkey PRIMARY KEY (id)
);

SET @DEFAULT_UUID = UUID();

INSERT INTO iris_message_folder
VALUES
       (@DEFAULT_UUID, 'Posteingang', null, 'INBOX', current_timestamp, current_timestamp),
       (UUID(), 'Postausgang', null, 'OUTBOX', current_timestamp, current_timestamp);

INSERT INTO iris_message_folder_default VALUES (@DEFAULT_UUID);

CREATE TABLE  iris_message (
    id uuid NOT NULL,
    folder_id uuid NOT NULL,
    subject varchar(500) NOT NULL,
    body varchar(6000) NOT NULL,
    hd_author_id varchar(255) NOT NULL,
    hd_author_name varchar(255) NOT NULL,
    hd_recipient_id varchar(255) NOT NULL,
    hd_recipient_name varchar(255) NOT NULL,
    is_read bool NULL,
    created timestamp NOT NULL,
    last_modified timestamp NOT NULL,
    CONSTRAINT iris_message_pkey PRIMARY KEY (id),
    CONSTRAINT iris_message_folder_fk FOREIGN KEY (folder_id) REFERENCES iris_message_folder(id)
);
CREATE INDEX iris_message_id ON iris_message (id);

CREATE TABLE  iris_message_file (
    id uuid NOT NULL,
    message_id uuid NOT NULL,
    name varchar(255) NOT NULL,
    content blob NOT NULL,
    content_type varchar(50) NULL,
    created timestamp NOT NULL,
    last_modified timestamp NOT NULL,
    CONSTRAINT iris_message_file_pkey PRIMARY KEY (id),
    CONSTRAINT iris_message_fk FOREIGN KEY (message_id) REFERENCES iris_message(id)
);
CREATE INDEX iris_message_file_id ON iris_message_file (id);