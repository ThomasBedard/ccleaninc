-- Customers table
DROP TABLE IF EXISTS customers; 

CREATE TABLE IF NOT EXISTS customers (
    id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY, -- Primary key for internal use
    customer_id VARCHAR(36) NOT NULL UNIQUE,            -- External reference for API and foreign keys
    first_name VARCHAR(50),                      -- Optional first name
    last_name VARCHAR(50),                       -- Optional last name
    company_name VARCHAR(100),                   -- Optional company name
    email VARCHAR(100) UNIQUE NOT NULL,          -- Common email field
    phone_number VARCHAR(20) NOT NULL                    -- Common phone number field
);

-- Added an index to customer_id for foreign key references
CREATE INDEX idx_customer_id ON customers (customer_id);

-- Employee Table
DROP TABLE IF EXISTS employees;
CREATE TABLE IF NOT EXISTS employees (
    id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
    employee_id VARCHAR(36) NOT NULL UNIQUE,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone_number VARCHAR(20),
    is_active BOOLEAN DEFAULT TRUE,
    role VARCHAR(50)
    );

-- Admin Table
DROP TABLE IF EXISTS admins;
CREATE TABLE IF NOT EXISTS admins (
                                      id VARCHAR(36) PRIMARY KEY DEFAULT (UUID()),
    admin_id VARCHAR(36) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- Services Table
DROP TABLE IF EXISTS services;
CREATE TABLE IF NOT EXISTS services (
    id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
    service_id VARCHAR(36) NOT NULL UNIQUE,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    pricing DECIMAL(10, 2) NOT NULL,
    is_available BOOLEAN DEFAULT TRUE,
    category VARCHAR(50),
    duration_minutes INT
    );

-- Schedule Table
DROP TABLE IF EXISTS schedules;
CREATE TABLE IF NOT EXISTS schedules (
                                         id VARCHAR(36) PRIMARY KEY DEFAULT (UUID()),
    schedule_id VARCHAR(36) NOT NULL,
    employee_id VARCHAR(36) REFERENCES employees(employee_id),
    service_id VARCHAR(36) REFERENCES services(service_id),
    customer_id VARCHAR(36) REFERENCES customers(customer_id),
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    status VARCHAR(50) DEFAULT 'SCHEDULED',
    location VARCHAR(255)
    );

-- Feedback Table
DROP TABLE IF EXISTS feedback_threads;
CREATE TABLE IF NOT EXISTS feedback_threads(
                                               id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                               feedback_id VARCHAR(36) NOT NULL,
    customer_id VARCHAR(36) NOT NULL,
    stars INTEGER NOT NULL,
    content VARCHAR(120) NOT NULL,
    status VARCHAR(50)
    );


-- Drop existing appointments table
DROP TABLE IF EXISTS appointments;
-- Create Appointments Table
CREATE TABLE IF NOT EXISTS appointments (
    id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
    appointment_id VARCHAR(36) NOT NULL UNIQUE,
    customer_id VARCHAR(36),
    scheduled_date DATETIME,
    status VARCHAR(36),
    services VARCHAR(255),
    comments VARCHAR(255)
);
