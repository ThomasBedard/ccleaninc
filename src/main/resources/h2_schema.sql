-- ============================
-- Drop existing tables (if any)
-- ============================
DROP TABLE IF EXISTS schedules;
DROP TABLE IF EXISTS feedback_threads;
DROP TABLE IF EXISTS appointments;
DROP TABLE IF EXISTS services;
DROP TABLE IF EXISTS admins;
DROP TABLE IF EXISTS employees;
DROP TABLE IF EXISTS customers;

-- =========================
-- Create the Customers table
-- =========================
CREATE TABLE IF NOT EXISTS customers (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,        -- H2 style for identity
    customer_id VARCHAR(36) NOT NULL UNIQUE,      -- External reference
    first_name VARCHAR(50),                       -- Optional first name
    last_name VARCHAR(50),                        -- Optional last name
    company_name VARCHAR(100),                    -- Optional company name
    email VARCHAR(100) NOT NULL UNIQUE,           -- Required email
    phone_number VARCHAR(20) NULL,
    address VARCHAR(255) DEFAULT NULL
);

-- Create an index for customer_id
CREATE INDEX idx_customer_id ON customers (customer_id);

-- =======================
-- Create the Employees table
-- =======================
CREATE TABLE IF NOT EXISTS employees (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    employee_id VARCHAR(36) NOT NULL UNIQUE,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone_number VARCHAR(20),
    is_active BOOLEAN DEFAULT TRUE,
    role VARCHAR(50)
);

-- ===================
-- Create the Admins table
-- ===================
CREATE TABLE IF NOT EXISTS admins (
    -- Use RANDOM_UUID() to generate the primary key by default
    id VARCHAR(36) PRIMARY KEY DEFAULT (RANDOM_UUID()),
    admin_id VARCHAR(36) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================
-- Create the Services table
-- =====================
CREATE TABLE IF NOT EXISTS services (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    service_id VARCHAR(36) NOT NULL UNIQUE,
    title VARCHAR(100) NOT NULL,
    description CLOB,                -- TEXT in MySQL can be CLOB in H2
    pricing DECIMAL(10, 2) NOT NULL,
    is_available BOOLEAN DEFAULT TRUE,
    category VARCHAR(50),
    duration_minutes INT
);

-- =========================
-- Create the Schedules table
-- =========================
CREATE TABLE IF NOT EXISTS schedules (
    -- Use RANDOM_UUID() to generate the primary key by default
    id VARCHAR(36) PRIMARY KEY DEFAULT (RANDOM_UUID()),
    schedule_id VARCHAR(36) NOT NULL,
    employee_id VARCHAR(36) REFERENCES employees(employee_id),
    service_id VARCHAR(36) REFERENCES services(service_id),
    customer_id VARCHAR(36) REFERENCES customers(customer_id),
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    status VARCHAR(50) DEFAULT 'SCHEDULED',
    location VARCHAR(255)
);

-- ===========================
-- Create the Feedback Threads table
-- ===========================
CREATE TABLE IF NOT EXISTS feedback_threads(
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    feedback_id VARCHAR(36) NOT NULL,
    customer_id VARCHAR(36) NOT NULL,
    stars INTEGER NOT NULL,
    content VARCHAR(120) NOT NULL,
    status VARCHAR(50)
);

-- =========================
-- Create the Appointments table
-- =========================
CREATE TABLE IF NOT EXISTS appointments (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    appointment_id VARCHAR(36) NOT NULL UNIQUE,
    customer_first_name VARCHAR(50),
    customer_last_name VARCHAR(50),
    customer_id VARCHAR(36),
    scheduled_date TIMESTAMP,  -- Replacing DATETIME with TIMESTAMP
    status VARCHAR(36),
    services VARCHAR(255),
    comments VARCHAR(255)
);