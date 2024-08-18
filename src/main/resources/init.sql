CREATE TABLE IF NOT EXISTS role (
    id INT NOT NULL PRIMARY KEY,
    "name" VARCHAR(255),
    "created_date" TIMESTAMP,
    "last_modified_date" TIMESTAMP
);

INSERT INTO role(id, name, created_date, last_modified_date)
SELECT 1, 'USER', NOW(), NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM role WHERE id = 1
);

CREATE TABLE IF NOT EXISTS country (
    id INT NOT NULL PRIMARY KEY,
    code VARCHAR(10),
    "name" VARCHAR(255)
);

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

CREATE TABLE IF NOT EXISTS product_category (
    id INT NOT NULL PRIMARY KEY,
    category_name VARCHAR(255)
);

INSERT INTO product_category (id, category_name) values
(1, 'Crochet Toys'), (2, 'Crochet Decorations'), (3, 'Crochet Clothes');

CREATE TABLE IF NOT EXISTS product (
    id INT NOT NULL PRIMARY KEY,
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

INSERT INTO product (id, active, description, image_url, name, sku, unit_price, units_in_stock, category_id, rate, sales)
VALUES
(1, true, 'Zollie is a handcrafted toy made from yarn, perfect for children. Its soft texture and friendly appearance will provide hours of joy and play.', '/images/products/crochet-animals/zollie.jpg', 'Zollie - DIY Crochet Toy', 'ZOL-0001', 29.99, 50, 1, 1, 0),
(2, true, 'Dragon is a handcrafted toy made from yarn, perfect for children. Its soft texture and friendly appearance will provide hours of joy and play.', '/images/products/crochet-animals/dragon.jpg', 'Dragon - DIY Crochet Toy', 'DRA-0002', 39.99, 50, 1, 1, 0),
(3, true, 'Zebra is a handcrafted toy made from yarn, perfect for children. Its soft texture and friendly appearance will provide hours of joy and play.', '/images/products/crochet-animals/zebra.jpg', 'Zebra - DIY Crochet Toy', 'ZEB-0003', 29.99, 50, 1, 1, 0),
(4, true, 'Giraffe is a handcrafted toy made from yarn, perfect for children. Its soft texture and friendly appearance will provide hours of joy and play.', '/images/products/crochet-animals/giraffe.jpg', 'Giraffe - DIY Crochet Toy', 'GIR-0004', 29.99, 50, 1, 1, 0),
(5, true, 'Monkey is a handcrafted toy made from yarn, perfect for children. Its soft texture and friendly appearance will provide hours of joy and play.', '/images/products/crochet-animals/monkey.jpg', 'Monkey - DIY Crochet Toy', 'MON-0005', 29.99, 50, 1, 1, 0),
(6, true, 'Unicorn is a handcrafted toy made from yarn, perfect for children. Its soft texture and friendly appearance will provide hours of joy and play.', '/images/products/crochet-animals/unicorn.jpg', 'Unicorn - DIY Crochet Toy', 'UNI-0006', 39.99, 50, 1, 1, 0),
(7, true, 'Bird is a handcrafted toy made from yarn, perfect for children. Its soft texture and friendly appearance will provide hours of joy and play.', '/images/products/crochet-animals/bird.jpg', 'Bird - DIY Crochet Toy', 'BIR-0007', 24.99, 50, 1, 1, 0),
(8, true, 'Reindeer is a handcrafted toy made from yarn, perfect for children. Its soft texture and friendly appearance will provide hours of joy and play.', '/images/products/crochet-animals/reindeer.jpg', 'Reindeer - DIY Crochet Toy', 'REI-0008', 34.99, 50, 1, 1, 0),
(9, true, 'Turtle is a handcrafted toy made from yarn, perfect for children. Its soft texture and friendly appearance will provide hours of joy and play.', '/images/products/crochet-animals/turtle.jpg', 'Turtle - DIY Crochet Toy', 'TUR-0009', 29.99, 50, 1, 1, 0),
(10, true, 'Fox is a handcrafted toy made from yarn, perfect for children. Its soft texture and friendly appearance will provide hours of joy and play.', '/images/products/crochet-animals/fox.jpg', 'Fox - DIY Crochet Toy', 'FOX-0010', 29.99, 50, 1, 1, 0),
(11, true, 'Pumpkin is a handcrafted yarn decoration, perfect for adding warmth and charm to your home decor', '/images/products/crochet-decorations/pumpkin.jpg', 'Pumpkin - DIY Crochet Decoration', 'PUM-0011', 29.99, 50, 2, 1, 0),
(12, true, 'Flower is a handcrafted yarn decoration, perfect for adding warmth and charm to your home decor', '/images/products/crochet-decorations/flower.jpg', 'Flower - DIY Crochet Decoration', 'FLO-0012', 9.99, 50, 2, 1, 0),
(13, true, 'Coaster is a handcrafted yarn decoration, perfect for adding warmth and charm to your home decor', '/images/products/crochet-decorations/coaster.jpg', 'Coaster - DIY Crochet Decoration', 'COA-0013', 9.99, 50, 2, 1, 0),
(17, true, 'Snowflake is a handcrafted yarn decoration, perfect for adding warmth and charm to your home decor', '/images/products/crochet-decorations/snowflake.jpg', 'Snowflake - DIY Crochet Decoration', 'SNO-0017', 9.99, 50, 2, 1, 0),
(20, true, 'Christmas Pudding Bauble is a handcrafted yarn decoration, perfect for adding warmth and charm to your home decor', '/images/products/crochet-decorations/christmas-pudding-bauble.jpg', 'Christmas Pudding Bauble - DIY Crochet Decoration', 'PUD-0020', 14.99, 50, 2, 1, 0);

