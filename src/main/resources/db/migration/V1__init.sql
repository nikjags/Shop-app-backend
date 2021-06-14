CREATE TABLE product (
  id SERIAL PRIMARY KEY,
  product_name varchar2(100) NOT NULL,
  product_type varchar2(100) NOT NULL,
  material varchar2(100) NOT NULL,
  manufacturer varchar2(100) NOT NULL,
  description varchar2(1000),
  price int
);

CREATE TABLE customer (
  id SERIAL PRIMARY KEY,
  first_name varchar2(255) NOT NULL,
  last_name varchar2(255) NOT NULL,
  login varchar2(255) NOT NULL UNIQUE,
  email varchar2(255) NOT NULL
);

CREATE TABLE orders (
  id SERIAL PRIMARY KEY,
  customer_id int NOT NULL,
  ordered_time timestamp NOT NULL,
  delivered boolean NOT NULL
);

CREATE TABLE orders_product (
  order_id int NOT NULL,
  product_id int NOT NULL
);

CREATE TABLE stock (
  id SERIAL PRIMARY KEY,
  product_id int NOT NULL,
  size varchar2(10) NOT NULL,
  quantity int NOT NULL,
  CONSTRAINT UC_Person UNIQUE (product_id, size)
);

ALTER TABLE orders ADD FOREIGN KEY (customer_id) REFERENCES customer (id);

ALTER TABLE orders_product ADD FOREIGN KEY (order_id) REFERENCES orders (id);

ALTER TABLE orders_product ADD FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE;

ALTER TABLE stock ADD FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE;