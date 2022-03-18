CREATE TABLE  iris_message_data (
    id binary(255) NOT NULL,
    message_id binary(255) NOT NULL,
    discriminator varchar(255) NOT NULL,
    payload varchar(max) NOT NULL,
    description varchar(255) NOT NULL,
    is_imported bit NULL,
    created datetime2 NOT NULL,
    last_modified datetime2 NOT NULL,
    created_by binary(255) NULL,
    last_modified_by binary(255) NULL,
    CONSTRAINT iris_message_data_message_fk FOREIGN KEY (message_id) REFERENCES iris_message(id),
    FOREIGN KEY (created_by) REFERENCES user_accounts(user_id),
    FOREIGN KEY (last_modified_by) REFERENCES user_accounts(user_id)
);