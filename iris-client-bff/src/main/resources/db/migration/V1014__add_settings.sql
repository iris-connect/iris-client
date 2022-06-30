CREATE TABLE settings (
    name varchar(50) primary key,
    stored_value varchar(1000) NOT NULL,
    saved_at date NOT NULL
);
