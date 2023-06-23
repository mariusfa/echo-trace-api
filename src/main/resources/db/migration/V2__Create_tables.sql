CREATE TABLE echotraceschema.eventnames (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

GRANT SELECT, INSERT, UPDATE, DELETE ON echotraceschema.eventnames TO appuser;
GRANT USAGE, SELECT ON SEQUENCE echotraceschema.eventnames_id_seq TO appuser;

CREATE TABLE echotraceschema.events (
    id SERIAL PRIMARY KEY,
    eventname_id INTEGER NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

ALTER TABLE echotraceschema.events ADD CONSTRAINT fk_events_eventsnames_id FOREIGN KEY (eventname_id) REFERENCES echotraceschema.eventnames(id);
