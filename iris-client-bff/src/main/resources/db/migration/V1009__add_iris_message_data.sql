CREATE TABLE  iris_message_data (
    id uuid NOT NULL,
    message_id uuid NOT NULL,
    discriminator varchar(255) NOT NULL,
    payload varchar(999999) NOT NULL,
    description varchar(255) NOT NULL,
    is_imported bool NULL,
    created timestamp NOT NULL,
    last_modified timestamp NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT iris_message_data_message_fk FOREIGN KEY (message_id) REFERENCES iris_message(id)
);