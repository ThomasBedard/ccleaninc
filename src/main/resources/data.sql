-- Insert Customers
INSERT INTO customers (customer_id, first_name, last_name, email, phone_number, password_hash) 
VALUES 
(UUID(), 'John', 'Doe', 'john.doe@example.com', '123-456-7890', 'hashed_password_1'),
(UUID(), 'Jane', 'Smith', 'jane.smith@example.com', '123-456-7891', 'hashed_password_2');

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

-- Insert Orders (Transactions)
INSERT INTO orders (order_id, customer_id, employee_id, total_price, status) 
VALUES 
(UUID(), (SELECT customer_id FROM customers WHERE first_name = 'John' LIMIT 1), 
         (SELECT employee_id FROM employees WHERE first_name = 'Alice' LIMIT 1), 250.00, 'PENDING');

-- Insert Order Items (Multiple services for a single order)
INSERT INTO order_items (order_item_id, order_id, service_id, quantity, item_price)
VALUES 
(UUID(), (SELECT order_id FROM orders WHERE customer_id = (SELECT customer_id FROM customers WHERE first_name = 'John' LIMIT 1) LIMIT 1),
         (SELECT service_id FROM services WHERE title = 'Office Cleaning' LIMIT 1), 1, 100.00),
(UUID(), (SELECT order_id FROM orders WHERE customer_id = (SELECT customer_id FROM customers WHERE first_name = 'John' LIMIT 1) LIMIT 1),
         (SELECT service_id FROM services WHERE title = 'Floor Polishing' LIMIT 1), 1, 150.00);

-- Insert Schedule Entries
INSERT INTO schedules (schedule_id, employee_id, service_id, customer_id, start_time, end_time, status, location)
VALUES 
(UUID(), (SELECT employee_id FROM employees WHERE first_name = 'Alice' LIMIT 1),
         (SELECT service_id FROM services WHERE title = 'Office Cleaning' LIMIT 1),
         (SELECT customer_id FROM customers WHERE first_name = 'John' LIMIT 1), 
         NOW(), NOW() + INTERVAL 1 HOUR, 'SCHEDULED', 'Office 101');

-- Insert Feedback Entries
INSERT INTO feedback_threads (feedback_id, user_id, stars, content, status) VALUES
        ('fdbk-uuid-1', 'uuid-acc1', 5, 'Excellent cleaning service! Everything looks spotless.', 'INVISIBLE'),
        ('fdbk-uuid-2', 'uuid-acc2', 4, 'The cleaning was great, but they arrived a bit late.', 'INVISIBLE'),
        ('fdbk-uuid-3', 'uuid-acc3', 5, 'Amazing attention to detail! Will definitely hire again.', 'INVISIBLE'),
        ('fdbk-uuid-4', 'uuid-acc4', 3, 'Good service, but some areas were missed during the cleaning.', 'INVISIBLE'),
        ('fdbk-uuid-5', 'uuid-acc5', 4, 'Friendly staff and efficient cleaning, but the pricing is slightly high.', 'INVISIBLE');