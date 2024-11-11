--liquibase formatted sql
--changeset shop:6

INSERT INTO product_category (category_name) values
('Crochet Toys'), ('Crochet Decorations'), ('Crochet Clothes');