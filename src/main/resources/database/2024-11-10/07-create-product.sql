--liquibase formatted sql
--changeset shop:7

CREATE TABLE IF NOT EXISTS product (
    id SERIAL PRIMARY KEY,
    active BOOLEAN,
    date_created TIMESTAMP DEFAULT now(),
    description TEXT,
    image_url TEXT,
    last_updated TIMESTAMP DEFAULT now(),
    "name" VARCHAR(255),
    sku VARCHAR(255),
    unit_price NUMERIC,
    units_in_stock INT,
    category_id INT,
    rate NUMERIC,
    sales NUMERIC,
    FOREIGN KEY (category_id) REFERENCES product_category(id)
);