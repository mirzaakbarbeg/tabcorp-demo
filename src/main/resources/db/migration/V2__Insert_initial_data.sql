
--Insert Customers
INSERT INTO customer (customer_id, first_name, last_name, email, location)
VALUES
  (10001, 'Tony', 'Stark', 'tony.stark@gmail.com', 'Australia'),
  (10002, 'Bruce', 'Banner', 'bruce.banner@gmail.com', 'US'),
  (10003, 'Steve', 'Rogers', 'steve.rogers@hotmail.com', 'Australia'),
  (10004, 'Wanda', 'Maximoff', 'wanda.maximoff@gmail.com', 'US'),
  (10005, 'Natasha', 'Romanoff', 'natasha.romanoff@gmail.com', 'Canada')
ON CONFLICT (customer_id) DO NOTHING;

-- Insert products
INSERT INTO product (product_code, cost, status)
VALUES
  ('PRODUCT_001', 50, 'Active'),
  ('PRODUCT_002', 100, 'Inactive'),
  ('PRODUCT_003', 200, 'Active'),
  ('PRODUCT_004', 10, 'Inactive'),
  ('PRODUCT_005', 500, 'Active')
ON CONFLICT (product_code) DO NOTHING;
