-- Création de la table si elle n'existe pas
CREATE TABLE IF NOT EXISTS patients (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        first_name VARCHAR(50),
                                        last_name VARCHAR(50),
                                        birthdate DATE,
                                        gender CHAR(1),
                                        address VARCHAR(100),
                                        phone_number VARCHAR(15)
);

-- Insertion de données initiales
INSERT INTO patients (first_name, last_name, birthdate, gender, address, phone_number)
VALUES
    ('John', 'Doe', '1980-01-01', 'M', '123 Main St', '5551234567'),
    ('Jane', 'Smith', '1990-05-15', 'F', '456 Elm St', '5559876543');