-- Insert Customers
INSERT INTO customers (
    customer_id, first_name, last_name, company_name, email, phone_number
) VALUES
-- Residential Customers
('c1d2e3f4-a5b6-11ec-82a8-0242ac130003', 'Vinicius', 'Velozo', NULL, 'viniciusvelozodesousa@gmail.com', '514-555-0101'),
('d3e4f5g6-b7c8-11ec-82a8-0242ac130003', 'Thomas', 'Bedard', NULL, 'champlaintbed@gmail.com', '514-555-0202'),
('e4f5g6h7-c8d9-11ec-82a8-0242ac130003', 'Paul', 'Lavoie', NULL, 'paul.lavoie@mail.com', '514-555-0303'),
('f5g6h7i8-d9e0-11ec-82a8-0242ac130003', 'Sophie', 'Girard', NULL, 'sophie.girard@mail.com', '514-555-0404'),
('g6h7i8j9-e0f1-11ec-82a8-0242ac130003', 'Lucas', 'Fortin', NULL, 'lucas.fortin@mail.com', '514-555-0505'),
('h7i8j9k0-f1g2-11ec-82a8-0242ac130003', 'John', 'Doe', NULL, 'john.doe@mail.com', '514-555-0606'),
('i8j9k0l1-g2h3-11ec-82a8-0242ac130003', 'Jane', 'Smith', NULL, 'jane.smith@mail.com', '514-555-0707'),

-- Industrial Customers
('j9k0l1m2-h3i4-11ec-82a8-0242ac130003', NULL, NULL, 'Bouchard Logistics Inc.', 'contact@bouchardlogistics.com', '514-555-0808'),
('k0l1m2n3-i4j5-11ec-82a8-0242ac130003', NULL, NULL, 'Lebeau Glass Experts', 'info@lebeau.com', '514-555-0909'),
('l1m2n3o4-j5k6-11ec-82a8-0242ac130003', 'Amélie', 'Roy', 'Roy & Partners Consulting', 'amelie.roy@royconsulting.com', '514-555-1010');

-- Insert Employees (with consistent employee_id)
INSERT INTO employees (employee_id, first_name, last_name, email, phone_number, is_active, role) 
VALUES 
('c1d2e3f4-a5b6-11ec-82a8-0242ac130003', 'Alice', 'Johnson', 'alice.johnson@example.com', '123-456-7892', TRUE, 'cleaner'),
('d3e4f5g6-b7c8-11ec-82a8-0242ac130003', 'Bob', 'Williams', 'bob.williams@example.com', '123-456-7893', TRUE, 'supervisor');


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
         (SELECT service_id FROM services WHERE title = 'Residential Cleaning Service' LIMIT 1),
         (SELECT customer_id FROM customers WHERE first_name = 'John' LIMIT 1), 
         NOW(), NOW() + INTERVAL 1 HOUR, 'SCHEDULED', 'Office 101');

-- Insert Feedback Entries with subselect to match customers by their known attributes
INSERT INTO feedback_threads (feedback_id, customer_id, stars, content, status)
VALUES
    ('fdbk-uuid-1', (SELECT customer_id FROM customers WHERE first_name = 'Vinicius' AND last_name = 'Velozo' LIMIT 1), 5, 'Excellent cleaning service! Everything looks spotless.', 'INVISIBLE'),
('fdbk-uuid-2', (SELECT customer_id FROM customers WHERE first_name = 'Thomas' AND last_name = 'Bedard' LIMIT 1), 4, 'The cleaning was great, but they arrived a bit late.', 'INVISIBLE'),
('fdbk-uuid-3', (SELECT customer_id FROM customers WHERE first_name = 'Paul' AND last_name = 'Lavoie' LIMIT 1), 5, 'Amazing attention to detail! Will definitely hire again.', 'INVISIBLE'),
('fdbk-uuid-4', (SELECT customer_id FROM customers WHERE first_name = 'Sophie' AND last_name = 'Girard' LIMIT 1), 3, 'Good service, but some areas were missed during the cleaning.', 'INVISIBLE'),
('fdbk-uuid-5', (SELECT customer_id FROM customers WHERE first_name = 'Lucas' AND last_name = 'Fortin' LIMIT 1), 4, 'Friendly staff and efficient cleaning, but the pricing is slightly high.', 'VISIBLE');


-- Insert 6 rows into the appointments table
INSERT INTO appointments (appointment_id, customer_first_name, customer_last_name, customer_id, scheduled_date, status, services, comments) VALUES
('a1b2c3d4-e5f6-11ec-82a8-0242ac130003', 'Vinicius', 'Velozo', 'c1d2e3f4-a5b6-11ec-82a8-0242ac130003', '2025-01-15 10:30:00', 'pending', 'Residential Cleaning Service', 'Customer prefers morning appointments'),
('b2c3d4e5-f6a7-11ec-82a8-0242ac130003', 'Thomas', 'Bedard', 'd3e4f5g6-b7c8-11ec-82a8-0242ac130003', '2025-01-16 15:00:00', 'pending', 'Commercial Cleaning Service', 'Customer requested specific stylist'),
('c3d4e5f6-a7b8-11ec-82a8-0242ac130003', 'Paul', 'Lavoie', 'e4f5g6h7-c8d9-11ec-82a8-0242ac130003', '2025-01-17 09:00:00', 'pending', 'Post Construction Cleaning', 'Customer was very satisfied'),
('d4e5f6g7-b8c9-11ec-82a8-0242ac130003', 'Sophie', 'Girard', 'f5g6h7i8-d9e0-11ec-82a8-0242ac130003', '2025-01-18 14:00:00', 'pending', 'Detailed Deep Cleaning', 'Cancelled due to customer illness'),
('e5f6g7h8-c9d0-11ec-82a8-0242ac130003', 'Lucas', 'Fortin', 'g6h7i8j9-e0f1-11ec-82a8-0242ac130003', '2025-01-19 11:00:00', 'pending', 'Detailed Deep Cleaning', 'Requested vegan-friendly products'),
('f6g7h8i9-d0e1-11ec-82a8-0242ac130003', 'John', 'Doe', 'h7i8j9k0-f1g2-11ec-82a8-0242ac130003', '2025-01-20 13:00:00', 'pending', 'Detailed Deep Cleaning', 'Customer requested express service');

INSERT INTO employee_schedule_availabilities 
(availability_id, employee_first_name, employee_last_name, employee_id, shift, available_date, comments)
VALUES

-- -----------------------------------------------------------------------
-- Availabilities for Alice Johnson
-- -----------------------------------------------------------------------
('a1b2c3d4-e5f6-11ec-82a8-0242ac130010', 'Alice', 'Johnson', 'c1d2e3f4-a5b6-11ec-82a8-0242ac130003', 
 'MORNING', '2025-02-15 08:00:00', 'Prefers morning shifts on weekdays'),

('b2c3d4e5-f6a7-11ec-82a8-0242ac130010', 'Alice', 'Johnson', 'c1d2e3f4-a5b6-11ec-82a8-0242ac130003', 
 'EVENING', '2025-02-16 16:00:00', 'Available for evening shifts after 4 PM'),

('c3d4e5f6-a7b8-11ec-82a8-0242ac130010', 'Alice', 'Johnson', 'c1d2e3f4-a5b6-11ec-82a8-0242ac130003',
 'NIGHT',   '2025-02-17 00:00:00', 'Only available for night shifts on weekends'),

-- -----------------------------------------------------------------------
-- Availabilities for Bob Williams
-- -----------------------------------------------------------------------
('d4e5f6g7-b8c9-11ec-82a8-0242ac130010', 'Bob', 'Williams', 'd3e4f5g6-b7c8-11ec-82a8-0242ac130003', 
 'MORNING', '2025-02-16 08:00:00', 'Prefers working morning shifts'),

('e5f6g7h8-c9d0-11ec-82a8-0242ac130010', 'Bob', 'Williams', 'd3e4f5g6-b7c8-11ec-82a8-0242ac130003', 
 'EVENING', '2025-02-17 16:00:00', 'Can work evening shifts occasionally'),

('f6g7h8i9-d0e1-11ec-82a8-0242ac130010', 'Bob', 'Williams', 'd3e4f5g6-b7c8-11ec-82a8-0242ac130003', 
 'NIGHT',   '2025-02-18 00:00:00', 'Prefers not to work late-night shifts but available if needed');

-- Insert Employee Schedules for Alice Johnson
INSERT INTO employee_schedules (
    schedule_id, employee_id, availability_id, assigned_date, shift, status, comments
) VALUES 
('a1111111-b222-4333-c444-555555555555', 'c1d2e3f4-a5b6-11ec-82a8-0242ac130003', 'a1b2c3d4-e5f6-11ec-82a8-0242ac130010', '2025-02-15 08:00:00', 'MORNING', 'APPROVED', 'Assigned based on morning preference'),
('b1111111-b222-4333-c444-555555555555', 'c1d2e3f4-a5b6-11ec-82a8-0242ac130003', 'b2c3d4e5-f6a7-11ec-82a8-0242ac130010', '2025-02-16 16:00:00', 'EVENING', 'APPROVED', 'Assigned evening shift');

-- Insert Employee Schedules for Bob Williams
INSERT INTO employee_schedules (
    schedule_id, employee_id, availability_id, assigned_date, shift, status, comments
) VALUES 
('c1111111-b222-4333-c444-555555555555', 'd3e4f5g6-b7c8-11ec-82a8-0242ac130003', 'd4e5f6g7-b8c9-11ec-82a8-0242ac130010', '2025-02-16 08:00:00', 'MORNING', 'APPROVED', 'Assigned based on morning preference'),
('d1111111-b222-4333-c444-555555555555', 'd3e4f5g6-b7c8-11ec-82a8-0242ac130003', 'e5f6g7h8-c9d0-11ec-82a8-0242ac130010', '2025-02-17 16:00:00', 'EVENING', 'APPROVED', 'Assigned evening shift');