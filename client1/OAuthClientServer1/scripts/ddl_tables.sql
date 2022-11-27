CREATE TABLE users (
    user_id serial NOT NULL PRIMARY KEY,
    username varchar NOT NULL UNIQUE,
	password varchar NOT NULL,
	email varchar NOT NULL UNIQUE,
	first_name varchar NULL,
	surname varchar NULL,
	birth_date date NULL
);
