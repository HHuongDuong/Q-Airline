-- Drop and create database
DROP DATABASE IF EXISTS qairline;
CREATE DATABASE qairline;
USE qairline;

-- Drop existing tables if they exist
DROP TABLE IF EXISTS flight_delays;
DROP TABLE IF EXISTS tickets;
DROP TABLE IF EXISTS flights;
DROP TABLE IF EXISTS news;
DROP TABLE IF EXISTS users;

-- Create users table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    full_name VARCHAR(100),
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create flights table
CREATE TABLE flights (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    flight_number VARCHAR(10) NOT NULL UNIQUE,
    departure_city VARCHAR(50) NOT NULL,
    arrival_city VARCHAR(50) NOT NULL,
    departure_time DATETIME NOT NULL,
    arrival_time DATETIME NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create tickets table
CREATE TABLE tickets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    flight_id BIGINT NOT NULL,
    seat_number VARCHAR(10) NOT NULL,
    status VARCHAR(20) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (flight_id) REFERENCES flights(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create news table
CREATE TABLE news (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    type VARCHAR(20) NOT NULL,
    publish_date DATETIME,
    expiry_date DATETIME,
    active BOOLEAN DEFAULT TRUE,
    created_by BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create flight_delays table
CREATE TABLE flight_delays (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    flight_id BIGINT NOT NULL,
    delay_duration INT NOT NULL,
    reason TEXT,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (flight_id) REFERENCES flights(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert default admin user (password: admin123)
INSERT INTO users (username, password, email, full_name, role)
VALUES ('admin', '$2a$10$rDkPvvAFV6GgJkKqGZxrUeYQZQZQZQZQZQZQZQZQZQZQZQZQZQZQ', 'admin@qairline.com', 'System Administrator', 'ADMIN');

-- Insert sample flights
INSERT INTO flights (flight_number, departure_city, arrival_city, departure_time, arrival_time, price, status)
VALUES 
('QA001', 'Hanoi', 'Ho Chi Minh City', '2024-03-20 08:00:00', '2024-03-20 10:00:00', 1500000, 'SCHEDULED'),
('QA002', 'Ho Chi Minh City', 'Hanoi', '2024-03-20 14:00:00', '2024-03-20 16:00:00', 1500000, 'SCHEDULED'),
('QA003', 'Hanoi', 'Da Nang', '2024-03-20 09:00:00', '2024-03-20 10:30:00', 1000000, 'SCHEDULED'),
('QA004', 'Da Nang', 'Hanoi', '2024-03-20 11:00:00', '2024-03-20 12:30:00', 1000000, 'SCHEDULED'),
('QA005', 'Ho Chi Minh City', 'Da Nang', '2024-03-20 13:00:00', '2024-03-20 14:30:00', 1200000, 'SCHEDULED');

-- Insert sample news
INSERT INTO news (title, content, type, publish_date, expiry_date, active, created_by)
VALUES 
('Welcome to Q-Airline', 'Welcome to our new airline service! We are committed to providing the best flying experience for our customers.', 'NEWS', '2024-03-20 00:00:00', '2024-12-31 23:59:59', true, 1),
('Summer Promotion', 'Get 20% off on all flights this summer! Book your tickets now and enjoy our special summer deals.', 'PROMOTION', '2024-03-20 00:00:00', '2024-08-31 23:59:59', true, 1),
('New Routes Added', 'We are excited to announce new routes connecting major cities in Vietnam. Check out our updated flight schedule!', 'NEWS', '2024-03-20 00:00:00', '2024-12-31 23:59:59', true, 1);

-- Insert sample flight delays
INSERT INTO flight_delays (flight_id, delay_duration, reason, status)
VALUES 
(1, 30, 'Technical maintenance', 'RESOLVED'),
(2, 45, 'Weather conditions', 'PENDING'); 