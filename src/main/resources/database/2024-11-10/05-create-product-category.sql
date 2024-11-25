--liquibase formatted sql
--changeset shop:5

CREATE TABLE IF NOT EXISTS product_category (
    id SERIAL PRIMARY KEY,
    category_name VARCHAR(255)
);