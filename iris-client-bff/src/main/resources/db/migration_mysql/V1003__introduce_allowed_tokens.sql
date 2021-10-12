CREATE TABLE allowed_tokens (
    jwt_token_digest varchar(255) primary key,
    user_name varchar(50) NOT NULL,
    expiration_time timestamp NOT NULL,
    created timestamp default now()
);