
CREATE TABLE  iris_message_folder (
    id uuid NOT NULL,
    name varchar(200) NOT NULL,
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
    author_hd_id varchar(200) NOT NULL,
    author_hd_name varchar(200) NOT NULL,
    recipient_hd_id varchar(200) NOT NULL,
    recipient_hd_name varchar(200) NOT NULL,
    is_read bool NULL,
    has_attachments bool NULL,
    created timestamp NOT NULL,
    last_modified timestamp NOT NULL,
    CONSTRAINT iris_message_pkey PRIMARY KEY (id),
    CONSTRAINT iris_message_folder_fk FOREIGN KEY (folder_id) REFERENCES iris_message_folder(id)
);
CREATE INDEX iris_message_id ON iris_message (id);
