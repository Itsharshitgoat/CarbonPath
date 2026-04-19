-- Run this file in your MySQL terminal or MySQL Workbench to setup the database manually
-- Command line usage: mysql -u root -p < database.sql

-- 1. Create the database
CREATE DATABASE IF NOT EXISTS carbon_path;

-- 2. Use the database
USE carbon_path;

-- 3. Create the trips table
CREATE TABLE IF NOT EXISTS trips (
    id INT PRIMARY KEY AUTO_INCREMENT,
    distance DOUBLE,
    transport VARCHAR(50),
    carbon DOUBLE,
    suggested_transport VARCHAR(50),
    potential_saving DOUBLE,
    date DATETIME DEFAULT CURRENT_TIMESTAMP
);
