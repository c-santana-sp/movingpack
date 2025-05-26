CREATE TABLE postal_code_history
(
    id          SERIAL PRIMARY KEY,
    postal_code VARCHAR,
    status_code INTEGER,
    payload     JSONB,
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
