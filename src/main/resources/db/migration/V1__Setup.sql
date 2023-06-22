CREATE SCHEMA echotraceschema;

CREATE USER appuser WITH PASSWORD 'password';

GRANT USAGE ON SCHEMA echotraceschema TO appuser;

ALTER USER appuser SET search_path TO echotraceschema;

CREATE TABLE echotraceschema.eventnames (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

GRANT SELECT, INSERT, UPDATE, DELETE ON echotraceschema.eventnames TO appuser;
GRANT USAGE, SELECT ON SEQUENCE echotraceschema.eventnames_id_seq TO appuser;
