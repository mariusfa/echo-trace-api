CREATE TABLE echotraceschema.name (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

GRANT SELECT, INSERT, UPDATE, DELETE ON echotraceschema.name TO appuser;
GRANT USAGE, SELECT ON SEQUENCE echotraceschema.name_id_seq TO appuser;

CREATE TABLE echotraceschema.event (
    id SERIAL PRIMARY KEY,
    name_id INTEGER NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

ALTER TABLE echotraceschema.event ADD CONSTRAINT fk_event_name_id FOREIGN KEY (name_id) REFERENCES echotraceschema.name(id);
