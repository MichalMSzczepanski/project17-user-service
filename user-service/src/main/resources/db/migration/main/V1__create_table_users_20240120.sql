CREATE TABLE IF NOT EXISTS "users"
(
    id           UUID PRIMARY KEY,
    email        VARCHAR(255) UNIQUE NOT NULL,
    password     VARCHAR(255)        NOT NULL,
    type         VARCHAR(50)         NOT NULL,
    phone_number VARCHAR(50)         NOT NULL,
    created_at   TIMESTAMP           NOT NULL,
    updated_at   TIMESTAMP           NOT NULL
);