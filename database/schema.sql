CREATE DATABASE IF NOT EXISTS sunrise_dental_clinic
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE sunrise_dental_clinic;

CREATE TABLE IF NOT EXISTS staff_user (
    user_id        INT AUTO_INCREMENT PRIMARY KEY,
    username        VARCHAR(50)  NOT NULL UNIQUE,
    password_hash   VARCHAR(255) NOT NULL,
    role            VARCHAR(30)  NOT NULL
);

CREATE TABLE IF NOT EXISTS patient (
    patient_id      INT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(100) NOT NULL,
    address         VARCHAR(255),
    contact_number  VARCHAR(20)  NOT NULL
);

CREATE TABLE IF NOT EXISTS dentist (
    dentist_id      INT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(100) NOT NULL,
    specialization  VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS treatment_type (
    treatment_type_id INT AUTO_INCREMENT PRIMARY KEY,
    name              VARCHAR(100)   NOT NULL,
    base_fee          DECIMAL(10,2)  NOT NULL
);

CREATE TABLE IF NOT EXISTS appointment (
    appointment_id      INT AUTO_INCREMENT PRIMARY KEY,
    appointment_number  VARCHAR(20) NOT NULL UNIQUE,
    patient_id          INT NOT NULL,
    dentist_id          INT NOT NULL,
    treatment_type_id   INT NOT NULL,
    appointment_date    DATE NOT NULL,
    appointment_time    TIME NOT NULL,
    status              VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED',
    CONSTRAINT fk_appointment_patient
        FOREIGN KEY (patient_id) REFERENCES patient(patient_id),
    CONSTRAINT fk_appointment_dentist
        FOREIGN KEY (dentist_id) REFERENCES dentist(dentist_id),
    CONSTRAINT fk_appointment_treatment_type
        FOREIGN KEY (treatment_type_id) REFERENCES treatment_type(treatment_type_id)
);

CREATE TABLE IF NOT EXISTS bill (
    bill_id            INT AUTO_INCREMENT PRIMARY KEY,
    appointment_id      INT NOT NULL UNIQUE,
    consultation_fee    DECIMAL(10,2) NOT NULL,
    treatment_cost       DECIMAL(10,2) NOT NULL,
    total_amount         DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_bill_appointment
        FOREIGN KEY (appointment_id) REFERENCES appointment(appointment_id)
);

INSERT INTO treatment_type (name, base_fee) VALUES
    ('Routine Check-up', 500.00),
    ('Teeth Cleaning', 1500.00),
    ('Root Canal', 5000.00),
    ('Tooth Extraction', 2500.00);

INSERT INTO dentist (name, specialization) VALUES
    ('Dr. Perera', 'General Dentistry'),
    ('Dr. Fernando', 'Orthodontics');
