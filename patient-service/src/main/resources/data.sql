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
    ('Test', 'None', '1966-12-31', 'F', '1 Brookside St', '100-222-3333'),
    ('Test', 'Borderline', '1945-06-24', 'M', '2 High St', '200-333-4444'),
    ('Test', 'InDanger', '2004-06-18', 'M', '3 Club Road', '300-444-5555'),
    ('Test', 'EarlyOnset', '2002-06-28', 'F', '4 Valley Dr', '400-555-6666');
