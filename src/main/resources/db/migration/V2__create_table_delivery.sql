CREATE TABLE delivery
(
    id            SERIAL PRIMARY KEY,
    postal_code   VARCHAR(255) NOT NULL,
    delivery_time TIMESTAMP    NOT NULL,
    driver_id     BIGINT,
    CONSTRAINT fk_driver
        FOREIGN KEY (driver_id)
            REFERENCES driver (id)
            ON DELETE SET NULL
);