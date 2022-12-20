CREATE TABLE users (
    user_id serial NOT NULL PRIMARY KEY,
    username varchar NULL,
	email varchar NULL,
	first_name varchar NULL,
	surname varchar NULL,
	birth_date date NULL
);
