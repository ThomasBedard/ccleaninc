-- Insert Customers
INSERT INTO customers (
    customer_id, first_name, last_name, company_name, email, phone_number
) VALUES
-- Residential Customers
(UUID(), 'Jean', 'Tremblay', NULL, 'jean.tremblay@mail.com', '514-555-0101'),
(UUID(), 'Marie', 'Dubois', NULL, 'marie.dubois@mail.com', '514-555-0202'),
(UUID(), 'Paul', 'Lavoie', NULL, 'paul.lavoie@mail.com', '514-555-0303'),
(UUID(), 'Sophie', 'Girard', NULL, 'sophie.girard@mail.com', '514-555-0404'),
(UUID(), 'Lucas', 'Fortin', NULL, 'lucas.fortin@mail.com', '514-555-0505'),
(UUID(), 'John', 'Doe', NULL, 'john.doe@mail.com', '514-555-0606'),
(UUID(), 'Jane', 'Smith', NULL, 'jane.smith@mail.com', '514-555-0707'),

-- Industrial Customers
(UUID(), NULL, NULL, 'Bouchard Logistics Inc.', 'contact@bouchardlogistics.com', '514-555-0808'),
(UUID(), NULL, NULL, 'Lebeau Glass Experts', 'info@lebeau.com', '514-555-0909'),
(UUID(), 'Amélie', 'Roy', 'Roy & Partners Consulting', 'amelie.roy@royconsulting.com', '514-555-1010');

-- Insert Employees
INSERT INTO employees (employee_id, first_name, last_name, email, phone_number, password_hash, is_active, role) 
VALUES 
(UUID(), 'Alice', 'Johnson', 'alice.johnson@example.com', '123-456-7892', 'hashed_password_3', TRUE, 'cleaner'),
(UUID(), 'Bob', 'Williams', 'bob.williams@example.com', '123-456-7893', 'hashed_password_4', TRUE, 'supervisor');

-- Insert Admins
INSERT INTO admins (admin_id, first_name, last_name, email, password_hash) 
VALUES 
(UUID(), 'Admin', 'User', 'admin@example.com', 'admin_password_hash');

-- Insert Services
INSERT INTO services (service_id, title, description, pricing, category, duration_minutes)
VALUES
    (UUID(), 'Residential Cleaning Service', 'A comprehensive cleaning for houses', 100.00, 'Standard', 60),
    (UUID(), 'Commercial Cleaning Service', 'A comprehensive cleaning for business', 0.0, 'Premium', 90),
    (UUID(), 'Post Construction Cleaning', 'A comprehensive cleaning for business', 100.00, 'Standard', 60),
    (UUID(), 'Detailed Deep Cleaning', 'A comprehensive cleaning for offices', 100.00, 'Standard', 60);

-- Insert Schedule Entries
INSERT INTO schedules (schedule_id, employee_id, service_id, customer_id, start_time, end_time, status, location)
VALUES 
(UUID(), (SELECT employee_id FROM employees WHERE first_name = 'Alice' LIMIT 1),
         (SELECT service_id FROM services WHERE title = 'Office Cleaning' LIMIT 1),
         (SELECT customer_id FROM customers WHERE first_name = 'John' LIMIT 1), 
         NOW(), NOW() + INTERVAL 1 HOUR, 'SCHEDULED', 'Office 101');

-- Insert Feedback Entries with subselect to match customers by their known attributes
INSERT INTO feedback_threads (feedback_id, customer_id, stars, content, status)
VALUES
    ('fdbk-uuid-1', (SELECT customer_id FROM customers WHERE first_name = 'Jean' AND last_name = 'Tremblay' LIMIT 1), 5, 'Excellent cleaning service! Everything looks spotless.', 'INVISIBLE'),
('fdbk-uuid-2', (SELECT customer_id FROM customers WHERE first_name = 'Marie' AND last_name = 'Dubois' LIMIT 1), 4, 'The cleaning was great, but they arrived a bit late.', 'INVISIBLE'),
('fdbk-uuid-3', (SELECT customer_id FROM customers WHERE first_name = 'Paul' AND last_name = 'Lavoie' LIMIT 1), 5, 'Amazing attention to detail! Will definitely hire again.', 'INVISIBLE'),
('fdbk-uuid-4', (SELECT customer_id FROM customers WHERE first_name = 'Sophie' AND last_name = 'Girard' LIMIT 1), 3, 'Good service, but some areas were missed during the cleaning.', 'INVISIBLE'),
('fdbk-uuid-5', (SELECT customer_id FROM customers WHERE first_name = 'Lucas' AND last_name = 'Fortin' LIMIT 1), 4, 'Friendly staff and efficient cleaning, but the pricing is slightly high.', 'VISIBLE');

-- Insert Appointments
INSERT INTO appointments (
    appointment_id,
    customer_id, 
    scheduled_date, 
    scheduled_time, 
    status, 
    notes, 
    service_ids
) VALUES 
(
    UUID(),
    (SELECT customer_id FROM customers WHERE first_name = 'Jean' LIMIT 1),
    '2024-02-15',
    '09:00:00',
    'pending',
    'Please use eco-friendly cleaning products',
    JSON_ARRAY(
        (SELECT service_id FROM services WHERE title = 'Residential Cleaning Service' LIMIT 1),
        (SELECT service_id FROM services WHERE title = 'Detailed Deep Cleaning' LIMIT 1)
    )
),
(
    UUID(),
    (SELECT customer_id FROM customers WHERE first_name = 'Marie' LIMIT 1),
    '2024-02-16',
    '14:00:00',
    'confirmed',
    'Cleaning after a party.',
    JSON_ARRAY(
        (SELECT service_id FROM services WHERE title = 'Commercial Cleaning Service' LIMIT 1)
    )
),
(
    UUID(),
    (SELECT customer_id FROM customers WHERE first_name = 'Paul' LIMIT 1),
    '2024-02-17',
    '10:30:00',
    'pending',
    'Deep clean entire apartment.',
    JSON_ARRAY(
        (SELECT service_id FROM services WHERE title = 'Detailed Deep Cleaning' LIMIT 1)
    )
),
(
    UUID(),
    (SELECT customer_id FROM customers WHERE first_name = 'Sophie' LIMIT 1),
    '2024-02-18',
    '11:00:00',
    'pending',
    'Post-construction cleaning.',
    JSON_ARRAY(
        (SELECT service_id FROM services WHERE title = 'Post Construction Cleaning' LIMIT 1)
    )
),
(
    UUID(),
    (SELECT customer_id FROM customers WHERE first_name = 'Lucas' LIMIT 1),
    '2024-02-20',
    '13:00:00',
    'confirmed',
    'Regular bi-weekly cleaning.',
    JSON_ARRAY(
        (SELECT service_id FROM services WHERE title = 'Residential Cleaning Service' LIMIT 1)
    )
),
(
    UUID(),
    (SELECT customer_id FROM customers WHERE first_name = 'John' LIMIT 1),
    '2024-02-22',
    '08:00:00',
    'pending',
    'Morning cleaning before office opens.',
    JSON_ARRAY(
        (SELECT service_id FROM services WHERE title = 'Commercial Cleaning Service' LIMIT 1)
    )
),
(
    UUID(),
    (SELECT customer_id FROM customers WHERE first_name = 'Jane' LIMIT 1),
    '2024-02-24',
    '15:00:00',
    'pending',
    'Preparing for guests, need extra thorough cleaning.',
    JSON_ARRAY(
        (SELECT service_id FROM services WHERE title = 'Detailed Deep Cleaning' LIMIT 1)
    )
),
(
    UUID(),
    (SELECT customer_id FROM customers WHERE company_name = 'Bouchard Logistics Inc.' LIMIT 1),
    '2024-02-26',
    '16:00:00',
    'confirmed',
    'Final cleaning after major renovation.',
    JSON_ARRAY(
        (SELECT service_id FROM services WHERE title = 'Post Construction Cleaning' LIMIT 1)
    )
),
(
    UUID(),
    (SELECT customer_id FROM customers WHERE company_name = 'Lebeau Glass Experts' LIMIT 1),
    '2024-02-28',
    '10:00:00',
    'pending',
    'Standard monthly cleaning.',
    JSON_ARRAY(
        (SELECT service_id FROM services WHERE title = 'Residential Cleaning Service' LIMIT 1)
    )
),
(
    UUID(),
    (SELECT customer_id FROM customers WHERE company_name = 'Roy & Partners Consulting' LIMIT 1),
    '2024-03-01',
    '17:00:00',
    'pending',
    'After-hours deep clean of conference rooms.',
    JSON_ARRAY(
        (SELECT service_id FROM services WHERE title = 'Commercial Cleaning Service' LIMIT 1)
    )
);
