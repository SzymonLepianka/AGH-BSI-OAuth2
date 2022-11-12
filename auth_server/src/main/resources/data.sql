INSERT INTO scopes (scope_id, name) VALUES (1, "user_email"), (2, "user_username"), (3, "user_firstname"), (4, "user_surname"), (5, "user_phonenumber"), (6, "user_birthdate");
INSERT INTO users (user_id, email, is_developer, password, username) VALUES (1,"admin@localhost", true, "$2a$10$/a7SLmCEgOFwkxUNGwnOYe4e6PkyZT58n3GAeKiXe5IvXwDn7vouC", "admin");
INSERT INTO client_apps (client_app_id, app_secret, redirecturl, user_id, age_restriction) VALUES (1,111111,"localhost:8081",1, 0), (2,222222,"localhost:8080",1, 0);
