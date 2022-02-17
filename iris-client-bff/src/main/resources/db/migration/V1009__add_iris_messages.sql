CREATE TABLE  iris_message_folder (
    id uuid NOT NULL,
    name varchar(255) NOT NULL,
    parent_folder uuid NULL,
    context varchar(50) NOT NULL,
    created timestamp NOT NULL,
    last_modified timestamp NOT NULL,
    created_by uuid NULL,
    last_modified_by uuid NULL,
    CONSTRAINT iris_message_folder_pkey PRIMARY KEY (id),
    FOREIGN KEY (created_by) REFERENCES user_accounts(user_id),
    FOREIGN KEY (last_modified_by) REFERENCES user_accounts(user_id)
);

CREATE TABLE  iris_message_folder_default (
    id uuid NOT NULL,
    CONSTRAINT iris_message_folder_default_pkey PRIMARY KEY (id)
);

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
    created_by uuid NULL,
    last_modified_by uuid NULL,
    CONSTRAINT iris_message_pkey PRIMARY KEY (id),
    CONSTRAINT iris_message_folder_fk FOREIGN KEY (folder_id) REFERENCES iris_message_folder(id),
    FOREIGN KEY (created_by) REFERENCES user_accounts(user_id),
    FOREIGN KEY (last_modified_by) REFERENCES user_accounts(user_id)
);
