CREATE TABLE patient (
    id INT PRIMARY KEY,
    firstname VARCHAR(255) NOT NULL,
    lastname VARCHAR(255) NOT NULL,
    birthdate VARCHAR(10) NOT NULL,
    gender VARCHAR(1) NOT NULL,
    address VARCHAR(255),
    phone VARCHAR(12)
)