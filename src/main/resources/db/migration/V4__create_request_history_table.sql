CREATE TABLE request_history (
    id                  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    request_id          TEXT NOT NULL UNIQUE,
    request_time        TIMESTAMP NOT NULL,
    external_service_id INTEGER NOT NULL REFERENCES external_service,
    client              VARCHAR(50) NOT NULL
);
