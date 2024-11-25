--liquibase formatted sql
--changeset shop:4

INSERT INTO country(id, code, name)
SELECT 1, 'PL', 'Poland'
WHERE NOT EXISTS (
    SELECT 1 FROM country WHERE code = 'PL'
);

INSERT INTO country(id, code, name)
SELECT 2, 'AT', 'Austria'
WHERE NOT EXISTS (
    SELECT 1 FROM country WHERE code = 'AT'
);

INSERT INTO country(id, code, name)
SELECT 3, 'BE', 'Belgium'
WHERE NOT EXISTS (
    SELECT 1 FROM country WHERE code = 'BE'
);

INSERT INTO country(id, code, name)
SELECT 4, 'BG', 'Bulgaria'
WHERE NOT EXISTS (
    SELECT 1 FROM country WHERE code = 'BG'
);

INSERT INTO country(id, code, name)
SELECT 5, 'CH', 'Switzerland'
WHERE NOT EXISTS (
    SELECT 1 FROM country WHERE code = 'CH'
);

INSERT INTO country(id, code, name)
SELECT 6, 'CY', 'Cyprus'
WHERE NOT EXISTS (
    SELECT 1 FROM country WHERE code = 'CY'
);

INSERT INTO country(id, code, name)
SELECT 7, 'CZ', 'Czech Republic'
WHERE NOT EXISTS (
    SELECT 1 FROM country WHERE code = 'CZ'
);

INSERT INTO country(id, code, name)
SELECT 8, 'DE', 'Germany'
WHERE NOT EXISTS (
    SELECT 1 FROM country WHERE code = 'DE'
);

INSERT INTO country(id, code, name)
SELECT 9, 'DK', 'Denmark'
WHERE NOT EXISTS (
    SELECT 1 FROM country WHERE code = 'DK'
);

INSERT INTO country(id, code, name)
SELECT 10, 'EE', 'Estonia'
WHERE NOT EXISTS (
    SELECT 1 FROM country WHERE code = 'EE'
);

INSERT INTO country(id, code, name)
SELECT 11, 'ES', 'Spain'
WHERE NOT EXISTS (
    SELECT 1 FROM country WHERE code = 'ES'
);

INSERT INTO country(id, code, name)
SELECT 12, 'FI', 'Finland'
WHERE NOT EXISTS (
    SELECT 1 FROM country WHERE code = 'FI'
);

INSERT INTO country(id, code, name)
SELECT 13, 'FR', 'France'
WHERE NOT EXISTS (
    SELECT 1 FROM country WHERE code = 'FR'
);

INSERT INTO country(id, code, name)
SELECT 14, 'GR', 'Greece'
WHERE NOT EXISTS (
    SELECT 1 FROM country WHERE code = 'GR'
);

INSERT INTO country(id, code, name)
SELECT 15, 'HU', 'Hungary'
WHERE NOT EXISTS (
    SELECT 1 FROM country WHERE code = 'HU'
);

INSERT INTO country(id, code, name)
SELECT 16, 'IE', 'Ireland'
WHERE NOT EXISTS (
    SELECT 1 FROM country WHERE code = 'IE'
);

INSERT INTO country(id, code, name)
SELECT 17, 'IT', 'Italy'
WHERE NOT EXISTS (
    SELECT 1 FROM country WHERE code = 'IT'
);

INSERT INTO country(id, code, name)
SELECT 18, 'LT', 'Lithuania'
WHERE NOT EXISTS (
    SELECT 1 FROM country WHERE code = 'LT'
);

INSERT INTO country(id, code, name)
SELECT 19, 'LU', 'Luxembourg'
WHERE NOT EXISTS (
    SELECT 1 FROM country WHERE code = 'LU'
);

INSERT INTO country(id, code, name)
SELECT 20, 'LV', 'Latvia'
WHERE NOT EXISTS (
    SELECT 1 FROM country WHERE code = 'LV'
);