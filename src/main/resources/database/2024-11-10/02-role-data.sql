--liquibase formatted sql
--changeset shop:2

INSERT INTO role(id, name, created_date, last_modified_date)
SELECT 1, 'USER', NOW(), NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM role WHERE id = 1
);

INSERT INTO role(id, name, created_date, last_modified_date)
SELECT 2, 'ADMIN', NOW(), NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM role WHERE id = 2
);