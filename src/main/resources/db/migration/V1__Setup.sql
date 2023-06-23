CREATE SCHEMA echotraceschema;

CREATE USER appuser WITH PASSWORD 'password';

GRANT USAGE ON SCHEMA echotraceschema TO appuser;

ALTER USER appuser SET search_path TO echotraceschema;
