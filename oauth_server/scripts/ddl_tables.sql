CREATE TABLE access_tokens (
    access_token_id serial NOT NULL PRIMARY KEY,
    user_id integer NOT NULL,
    client_app_id integer NOT NULL,
    scope varchar NULL,
    created_at timestamp NULL,
    updated_at timestamp NULL,
    expires_at timestamp NULL,
    revoked boolean NULL
);

CREATE TABLE auth_codes (
    auth_code_id serial NOT NULL PRIMARY KEY,
    user_id integer NOT NULL,
    client_app_id integer NOT NULL,
	revoked boolean NULL,
    content varchar NULL,
	expires_at timestamp NULL
);

CREATE TABLE client_apps (
    client_app_id serial NOT NULL PRIMARY KEY,
    user_id integer NOT NULL,
	app_secret integer NULL,
    redirect_url varchar NULL,
	age_restriction boolean NULL
);

CREATE TABLE permissions (
    permission_id serial NOT NULL PRIMARY KEY,
    user_id integer NOT NULL,
    client_app_id integer NOT NULL,
    scope_id integer NOT NULL
);

CREATE TABLE refresh_tokens (
    refresh_token_id serial NOT NULL PRIMARY KEY,
    access_token_id integer NOT NULL,
	revoked boolean NULL,
	expires_at timestamp NULL
);

CREATE TABLE scopes (
    scope_id serial NOT NULL PRIMARY KEY,
    name varchar NULL UNIQUE
);

CREATE TABLE users (
    user_id serial NOT NULL PRIMARY KEY,
    username varchar NOT NULL UNIQUE,
	password varchar NOT NULL,
	email varchar NOT NULL UNIQUE,
	first_name varchar NULL,
	surname varchar NULL,
	birth_date date NULL,
	phone_number varchar NULL,
	is_developer boolean NULL
);

ALTER TABLE access_tokens ADD CONSTRAINT access_tokens_users_fk FOREIGN KEY ("user_id") REFERENCES users(user_id);
ALTER TABLE access_tokens ADD CONSTRAINT access_tokens_client_apps_fk FOREIGN KEY ("client_app_id") REFERENCES client_apps(client_app_id);

ALTER TABLE auth_codes ADD CONSTRAINT auth_codes_users_fk FOREIGN KEY ("user_id") REFERENCES users(user_id);
ALTER TABLE auth_codes ADD CONSTRAINT auth_codes_client_apps_fk FOREIGN KEY ("client_app_id") REFERENCES client_apps(client_app_id);

ALTER TABLE client_apps ADD CONSTRAINT client_apps_users_fk FOREIGN KEY ("user_id") REFERENCES users(user_id);

ALTER TABLE permissions ADD CONSTRAINT permissions_users_fk FOREIGN KEY ("user_id") REFERENCES users(user_id);
ALTER TABLE permissions ADD CONSTRAINT permissions_client_apps_fk FOREIGN KEY ("client_app_id") REFERENCES client_apps(client_app_id);
ALTER TABLE permissions ADD CONSTRAINT permissions_scopes_fk FOREIGN KEY ("scope_id") REFERENCES scopes(scope_id);

ALTER TABLE refresh_tokens ADD CONSTRAINT refresh_tokens_access_tokens_fk FOREIGN KEY ("access_token_id") REFERENCES access_tokens(access_token_id);
