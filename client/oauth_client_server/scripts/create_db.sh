#!/bin/bash

# Poniższe komendy dotyczące stworzenia bazy, usera oraz schematu można wykonać z linii komend logując się na SuperUser:
# > psql -U postgres
# lub połączyć się z db za pomocą klienta np. DBeaver i wykonać te komendy

# Zrób bazę:
CREATE DATABASE oauth_client_1 WITH OWNER = postgres CONNECTION LIMIT = -1;

# Jeśli nie widać bazy w DBeaver to: 
## 1. Kliknąć prawym na dany Connection i wybrać 'Edit Connection'.
## 2. W zakładce 'PostgreSQL' zaznaczyć 'Show all databases'.

# Użytkownik bgs powinien być stworzony:
# CREATE USER bgs WITH LOGIN SUPERUSER INHERIT NOCREATEDB NOCREATEROLE NOREPLICATION PASSWORD 'password';
GRANT CONNECT ON DATABASE oauth_client_1 to bgs;
GRANT ALL ON DATABASE oauth_client_1 TO postgres;

# Zrób schemat:
\connect oauth_client_1
CREATE SCHEMA IF NOT EXISTS oauth AUTHORIZATION bgs;
