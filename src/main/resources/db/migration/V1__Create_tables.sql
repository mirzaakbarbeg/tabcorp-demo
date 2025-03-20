
CREATE TABLE IF NOT EXISTS customer (
    customer_id INT PRIMARY KEY,
    first_name VARCHAR(50),
    last_Name VARCHAR(50),
    email VARCHAR(100),
    location VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS product (
    product_code VARCHAR(50) PRIMARY KEY,
    cost INT,
    status VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS transactions (
    id BIGSERIAL PRIMARY KEY,
    transaction_time TIMESTAMP,
    quantity INT,
    product_code VARCHAR(50),
    customer_id INT,
    CONSTRAINT fk_product FOREIGN KEY (product_code) REFERENCES product(product_code),
    CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
);
