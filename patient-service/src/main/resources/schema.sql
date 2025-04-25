CREATE TABLE IF NOT EXISTS patient (
    id SERIAL PRIMARY KEY,
    firstname VARCHAR(255) NOT NULL,
    lastname VARCHAR(255) NOT NULL,
    birthdate VARCHAR(10) NOT NULL,
    gender VARCHAR(1) NOT NULL CHECK (gender IN ('M', 'F')),
    address VARCHAR(255),
    phone VARCHAR(12)
);