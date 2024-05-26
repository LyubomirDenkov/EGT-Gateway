CREATE TABLE currency_data (
    id               BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    base_currency_id INTEGER NOT NULL REFERENCES currency_code,
    updated_time     TIMESTAMP NOT NULL,
    rates            TEXT NOT NULL
);
