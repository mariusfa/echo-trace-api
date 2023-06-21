CREATE SCHEMA echotraceschema;

CREATE USER appuser WITH PASSWORD 'password';

GRANT USAGE ON SCHEMA echotraceschema TO appuser;

ALTER USER appuser SET search_path TO echotraceschema;

CREATE TABLE echotraceschema.events (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

GRANT SELECT, INSERT, UPDATE, DELETE ON echotraceschema.events TO appuser;
GRANT USAGE, SELECT ON SEQUENCE echotraceschema.events_id_seq TO appuser;
