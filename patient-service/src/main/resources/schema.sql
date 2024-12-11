CREATE TABLE patients (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    birthdate DATE NOT NULL,
    gender VARCHAR(1) NOT NULL,
    address VARCHAR(255) NOT NULL,
    phone_number VARCHAR(10)
)