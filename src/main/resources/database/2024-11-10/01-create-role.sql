--liquibase formatted sql
--changeset shop:1

CREATE TABLE IF NOT EXISTS role (
    id INT NOT NULL PRIMARY KEY,
    "name" VARCHAR(255),
    "created_date" TIMESTAMP,
    "last_modified_date" TIMESTAMP
);