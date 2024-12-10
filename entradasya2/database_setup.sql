-- Create database if it doesn't exist
CREATE DATABASE IF NOT EXISTS entradasya;

-- Use the database
USE entradasya;

-- Create clientes table
CREATE TABLE IF NOT EXISTS clientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL
);
