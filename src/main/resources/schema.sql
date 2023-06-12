CREATE TABLE IF NOT EXISTS medications (
    code VARCHAR(100) PRIMARY KEY,
    name VARCHAR(255),
    weight DECIMAL(10, 2),
    image VARCHAR(255)
);