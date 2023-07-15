CREATE TABLE echotraceschema.user (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    hashed_password VARCHAR(255) NOT NULL,
    api_token VARCHAR(255) NOT NULL UNIQUE
);

GRANT SELECT, INSERT, UPDATE, DELETE ON echotraceschema.user TO appuser;
GRANT USAGE, SELECT ON SEQUENCE echotraceschema.user_id_seq TO appuser;

CREATE TABLE echotraceschema.name (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    user_id INTEGER NOT NULL
);

ALTER TABLE echotraceschema.name ADD CONSTRAINT fk_name_user_id FOREIGN KEY (user_id) REFERENCES echotraceschema.user(id);

GRANT SELECT, INSERT, UPDATE, DELETE ON echotraceschema.name TO appuser;
GRANT USAGE, SELECT ON SEQUENCE echotraceschema.name_id_seq TO appuser;

CREATE TABLE echotraceschema.event (
    id SERIAL PRIMARY KEY,
    name_id INTEGER NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

ALTER TABLE echotraceschema.event ADD CONSTRAINT fk_event_name_id FOREIGN KEY (name_id) REFERENCES echotraceschema.name(id);

GRANT SELECT, INSERT, UPDATE, DELETE ON echotraceschema.event TO appuser;
GRANT USAGE, SELECT ON SEQUENCE echotraceschema.event_id_seq TO appuser;
