-- Database creation
CREATE DATABASE IF NOT EXISTS product_billing
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE product_billing;

-- Table: products
CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    description VARCHAR(500),
    price DECIMAL(10, 2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Table: bills
CREATE TABLE IF NOT EXISTS bills (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    total DECIMAL(12, 2) NOT NULL,
    created_at DATETIME(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Table: bill_items
CREATE TABLE IF NOT EXISTS bill_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    bill_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    product_name VARCHAR(120) NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    quantity INT NOT NULL,
    CONSTRAINT fk_bill_items_bill FOREIGN KEY (bill_id) REFERENCES bills(id) ON DELETE CASCADE,
    CONSTRAINT fk_bill_items_product FOREIGN KEY (product_id) REFERENCES products(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Optional initial seed products
INSERT INTO products (name, description, price) VALUES
('Espresso', 'Rich double shot espresso', 3.50),
('Cappuccino', 'Freshly brewed coffee with steamed milk foam', 4.50),
('Croissant', 'Flaky butter pastry baked fresh daily', 2.75),
('Matcha Green Tea Latte', 'Organic Japanese matcha with oat milk', 5.25)
ON DUPLICATE KEY UPDATE name=name;
