CREATE TABLE  iris_message_data (
    id binary(16) NOT NULL,
    message_id binary(16) NOT NULL,
    discriminator varchar(255) NOT NULL,
    payload varchar(999999) NOT NULL,
    description varchar(255) NOT NULL,
    is_imported bool NULL,
    created datetime NOT NULL,
    last_modified datetime NOT NULL,
    created_by binary(16) NULL,
    last_modified_by binary(16) NULL,
    CONSTRAINT iris_message_data_message_fk FOREIGN KEY (message_id) REFERENCES iris_message(id),
    FOREIGN KEY (created_by) REFERENCES user_accounts(user_id),
    FOREIGN KEY (last_modified_by) REFERENCES user_accounts(user_id)
);