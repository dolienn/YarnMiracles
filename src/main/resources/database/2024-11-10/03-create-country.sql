--liquibase formatted sql
--changeset shop:3

CREATE TABLE IF NOT EXISTS country (
    id INT NOT NULL PRIMARY KEY,
    code VARCHAR(10),
    "name" VARCHAR(255)
);