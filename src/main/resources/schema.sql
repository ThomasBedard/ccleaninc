-- Customer Table
DROP TABLE IF EXISTS customers;
CREATE TABLE IF NOT EXISTS customers (
                                         id VARCHAR(36) PRIMARY KEY DEFAULT (UUID()),
    customer_id VARCHAR(36) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone_number VARCHAR(20),
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP
    );

-- Employee Table
DROP TABLE IF EXISTS employees;
CREATE TABLE IF NOT EXISTS employees (
                                         id VARCHAR(36) PRIMARY KEY DEFAULT (UUID()),
    employee_id VARCHAR(36) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone_number VARCHAR(20),
    password_hash VARCHAR(255) NOT NULL,
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

-- Transactions/Orders Table
DROP TABLE IF EXISTS orders;
CREATE TABLE IF NOT EXISTS orders (
                                      id VARCHAR(36) PRIMARY KEY DEFAULT (UUID()),
    order_id VARCHAR(36) NOT NULL,
    customer_id VARCHAR(36) REFERENCES customers(customer_id),
    employee_id VARCHAR(36) REFERENCES employees(employee_id),
    total_price DECIMAL(10, 2),
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50) DEFAULT 'PENDING'
    );

-- Order Items (to handle multiple services per order)
DROP TABLE IF EXISTS order_items;
CREATE TABLE IF NOT EXISTS order_items (
                                           id VARCHAR(36) PRIMARY KEY DEFAULT (UUID()),
    order_item_id VARCHAR(36) NOT NULL,
    order_id VARCHAR(36) REFERENCES orders(order_id),
    service_id VARCHAR(36) REFERENCES services(service_id),
    quantity INT DEFAULT 1,
    item_price DECIMAL(10, 2)
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
