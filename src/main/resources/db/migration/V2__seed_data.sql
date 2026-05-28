-- V2__seed_data.sql
INSERT INTO categories (name) VALUES ('Electronics'), ('Clothing'), ('Books');

INSERT INTO products (name, description, price, stock, category_id) VALUES
('Laptop Pro',   'High-performance laptop',   75000.00, 10, 1),
('Smartphone X', '5G smartphone',             45000.00, 25, 1),
('Cotton T-Shirt','Comfortable everyday wear', 499.00,  100, 2),
('Java Basics',  'Intro to Java programming',  599.00,   50, 3);

INSERT INTO users (name, email, password, role) VALUES
('Admin User', 'admin@shop.com', '$2a$10$dummyhashhere', 'ADMIN'),
('John Doe',   'john@example.com', '$2a$10$dummyhashhere2', 'CUSTOMER');