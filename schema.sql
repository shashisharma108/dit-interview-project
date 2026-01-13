
-- CREATE DATABASE IF NOT EXISTS dit_db; -- USE when you have MySQL installed
-- USE dit_db; -- -- USE when you have MySQL installed

DROP TABLE IF EXISTS users cascade;
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    last_login_time TIMESTAMP NULL
);
