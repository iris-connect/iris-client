CREATE TABLE  iris_message_data (
    id uuid NOT NULL,
    message_id uuid NOT NULL,
    discriminator varchar(255) NOT NULL,
    payload bytea NOT NULL,
    description varchar(255) NOT NULL,
    is_imported bool NULL,
    created timestamp NOT NULL,
    last_modified timestamp NOT NULL,
    created_by uuid NULL,
    last_modified_by uuid NULL,
    CONSTRAINT iris_message_data_message_fk FOREIGN KEY (message_id) REFERENCES iris_message(id),
    FOREIGN KEY (created_by) REFERENCES user_accounts(user_id),
    FOREIGN KEY (last_modified_by) REFERENCES user_accounts(user_id)
);