-- Drop and create database
DROP DATABASE IF EXISTS qairline;
CREATE DATABASE qairline;
USE qairline;

-- Drop existing tables if they exist
DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS news;
DROP TABLE IF EXISTS flight_delays;
DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS seats;
DROP TABLE IF EXISTS flights;
DROP TABLE IF EXISTS aircraft;
DROP TABLE IF EXISTS airports;
DROP TABLE IF EXISTS users;

-- Create users table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    full_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20),
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create airports table
CREATE TABLE airports (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(3) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL,
    country VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create aircraft table
CREATE TABLE aircraft (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    registration_number VARCHAR(20) NOT NULL UNIQUE,
    type VARCHAR(50) NOT NULL,
    total_seats INT NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create flights table
CREATE TABLE flights (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    flight_number VARCHAR(10) NOT NULL UNIQUE,
    departure_airport_id BIGINT NOT NULL,
    arrival_airport_id BIGINT NOT NULL,
    aircraft_id BIGINT NOT NULL,
    departure_time DATETIME NOT NULL,
    arrival_time DATETIME NOT NULL,
    base_price DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (departure_airport_id) REFERENCES airports(id),
    FOREIGN KEY (arrival_airport_id) REFERENCES airports(id),
    FOREIGN KEY (aircraft_id) REFERENCES aircraft(id)
);

-- Create seats table
CREATE TABLE seats (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    flight_id BIGINT NOT NULL,
    seat_number VARCHAR(10) NOT NULL,
    seat_class VARCHAR(20) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (flight_id) REFERENCES flights(id),
    UNIQUE KEY unique_seat_flight (flight_id, seat_number)
);

-- Create bookings table
CREATE TABLE bookings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    flight_id BIGINT NOT NULL,
    seat_id BIGINT NOT NULL,
    booking_date TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (flight_id) REFERENCES flights(id),
    FOREIGN KEY (seat_id) REFERENCES seats(id)
);

-- Create flight_delays table
CREATE TABLE flight_delays (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    flight_id BIGINT NOT NULL,
    delay_duration INT NOT NULL,
    reason TEXT,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (flight_id) REFERENCES flights(id)
);

-- Create news table
CREATE TABLE news (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    type VARCHAR(50) NOT NULL,
    publish_date DATETIME,
    expiry_date DATETIME,
    active BOOLEAN DEFAULT TRUE,
    created_by BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES users(id)
);

-- Create payments table
CREATE TABLE payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_id BIGINT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL,
    transaction_id VARCHAR(100),
    payment_date TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (booking_id) REFERENCES bookings(id)
);

-- Create indexes for better performance
CREATE INDEX idx_flight_departure_time ON flights(departure_time);
CREATE INDEX idx_flight_status ON flights(status);
CREATE INDEX idx_seat_flight_status ON seats(flight_id, status);
CREATE INDEX idx_booking_user ON bookings(user_id);
CREATE INDEX idx_booking_status ON bookings(status);

-- Insert sample users
INSERT INTO users (username, password, email, full_name, phone_number, role)
VALUES 
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'admin@qairline.com', 'Admin User', '0123456789', 'ADMIN'),
('user1', '$2a$10$8K1p/a0dR1xqM8MCyJ3zQO3E3Z3Z3Z3Z3Z3Z3Z3Z3Z3Z3Z3Z3Z3Z3', 'user1@example.com', 'User One', '0123456781', 'USER'),
('user2', '$2a$10$8K1p/a0dR1xqM8MCyJ3zQO3E3Z3Z3Z3Z3Z3Z3Z3Z3Z3Z3Z3Z3Z3Z3', 'user2@example.com', 'User Two', '0123456782', 'USER');

-- Insert sample airports
INSERT INTO airports (code, name, city, country)
VALUES 
('HAN', 'Noi Bai International Airport', 'Hanoi', 'Vietnam'),
('SGN', 'Tan Son Nhat International Airport', 'Ho Chi Minh City', 'Vietnam'),
('DAD', 'Da Nang International Airport', 'Da Nang', 'Vietnam'),
('CXR', 'Cam Ranh International Airport', 'Nha Trang', 'Vietnam'),
('HUI', 'Phu Bai International Airport', 'Hue', 'Vietnam');

-- Insert sample aircraft
INSERT INTO aircraft (registration_number, type, total_seats, status)
VALUES 
('VN-A123', 'Boeing 737-800', 189, 'ACTIVE'),
('VN-B456', 'Airbus A320-200', 180, 'ACTIVE'),
('VN-C789', 'Boeing 787-9', 294, 'ACTIVE');

-- Insert sample flights
INSERT INTO flights (flight_number, departure_airport_id, arrival_airport_id, aircraft_id, departure_time, arrival_time, base_price, status)
VALUES 
('QA001', 1, 2, 1, '2024-03-20 08:00:00', '2024-03-20 10:00:00', 1500000, 'SCHEDULED'),
('QA002', 2, 1, 1, '2024-03-20 14:00:00', '2024-03-20 16:00:00', 1500000, 'SCHEDULED'),
('QA003', 1, 3, 2, '2024-03-20 09:00:00', '2024-03-20 10:30:00', 1000000, 'SCHEDULED'),
('QA004', 3, 1, 2, '2024-03-20 11:00:00', '2024-03-20 12:30:00', 1000000, 'SCHEDULED'),
('QA005', 2, 3, 3, '2024-03-20 13:00:00', '2024-03-20 14:30:00', 1200000, 'SCHEDULED');

-- Insert sample seats for each flight
INSERT INTO seats (flight_id, seat_number, seat_class, price, status)
SELECT 
    f.id,
    CONCAT(r.row_num, CHAR(64 + c.col_num)),
    CASE 
        WHEN r.row_num <= 3 THEN 'FIRST'
        WHEN r.row_num <= 8 THEN 'BUSINESS'
        ELSE 'ECONOMY'
    END,
    CASE 
        WHEN r.row_num <= 3 THEN f.base_price * 3
        WHEN r.row_num <= 8 THEN f.base_price * 2
        ELSE f.base_price
    END,
    'AVAILABLE'
FROM flights f
CROSS JOIN (
    SELECT 1 as row_num UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12 UNION SELECT 13 UNION SELECT 14 UNION SELECT 15 UNION SELECT 16 UNION SELECT 17 UNION SELECT 18 UNION SELECT 19 UNION SELECT 20
) r
CROSS JOIN (
    SELECT 1 as col_num UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6
) c
WHERE r.row_num <= 20 AND c.col_num <= 6;

-- Insert sample bookings
INSERT INTO bookings (user_id, flight_id, seat_id, booking_date, status, total_price)
VALUES 
(2, 1, 1, '2024-03-19 10:00:00', 'CONFIRMED', 4500000),
(3, 2, 2, '2024-03-19 11:00:00', 'CONFIRMED', 3000000);

-- Insert sample flight delays
INSERT INTO flight_delays (flight_id, delay_duration, reason, status)
VALUES 
(1, 30, 'Technical issue', 'ACTIVE'),
(2, 45, 'Weather conditions', 'ACTIVE');

-- Insert sample news
INSERT INTO news (title, content, type, publish_date, expiry_date, active, created_by)
VALUES 
('New Route Announcement', 'Q-Airline is pleased to announce new routes connecting major cities in Vietnam.', 'ANNOUNCEMENT', '2024-03-01 00:00:00', '2024-04-01 00:00:00', true, 1),
('Summer Promotion', 'Book your summer vacation with Q-Airline and get up to 20% off on selected routes.', 'PROMOTION', '2024-03-15 00:00:00', '2024-06-15 00:00:00', true, 1);

-- Insert sample payments
INSERT INTO payments (booking_id, amount, payment_method, status, transaction_id, payment_date)
VALUES 
(1, 4500000, 'CREDIT_CARD', 'COMPLETED', 'TXN123456', '2024-03-19 10:05:00'),
(2, 3000000, 'BANK_TRANSFER', 'COMPLETED', 'TXN789012', '2024-03-19 11:05:00'); 