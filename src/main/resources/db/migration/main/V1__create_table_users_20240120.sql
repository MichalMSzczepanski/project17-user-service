CREATE TABLE IF NOT EXISTS "users"
(
    id           UUID PRIMARY KEY,
    email        VARCHAR(255) UNIQUE NOT NULL,
    password     VARCHAR(255),
    user_type    VARCHAR(50),
    phone_number VARCHAR(50),
    active       BOOLEAN,
    created_at   TIMESTAMP,
    updated_at   TIMESTAMP
);