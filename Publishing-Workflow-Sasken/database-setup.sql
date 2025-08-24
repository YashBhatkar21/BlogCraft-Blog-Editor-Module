-- Database setup script for BlogCraft Publishing Workflow
-- Run this script in MySQL to create the database and user

-- Create the database
CREATE DATABASE IF NOT EXISTS blogcraft CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Use the database
USE blogcraft;

-- Create a user (optional - you can use root)
-- CREATE USER IF NOT EXISTS 'blogcraft_user'@'localhost' IDENTIFIED BY 'your_password';
-- GRANT ALL PRIVILEGES ON blogcraft.* TO 'blogcraft_user'@'localhost';
-- FLUSH PRIVILEGES;

-- The tables will be created automatically by Hibernate when you run the application
-- with spring.jpa.hibernate.ddl-auto=update

-- Verify the database was created
SHOW DATABASES;
SELECT DATABASE();
