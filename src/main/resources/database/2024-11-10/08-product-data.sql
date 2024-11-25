--liquibase formatted sql
--changeset shop:8

INSERT INTO product (active, description, image_url, name, sku, unit_price, units_in_stock, category_id, rate, sales)
VALUES
(true, 'Zollie is a handcrafted toy made from yarn, perfect for children. Its soft texture and friendly appearance will provide hours of joy and play.', '/images/products/crochet-animals/zollie.jpg', 'Zollie - DIY Crochet Toy', 'ZOL-0001', 29.99, 50, 1, 1, 0),
(true, 'Dragon is a handcrafted toy made from yarn, perfect for children. Its soft texture and friendly appearance will provide hours of joy and play.', '/images/products/crochet-animals/dragon.jpg', 'Dragon - DIY Crochet Toy', 'DRA-0002', 39.99, 50, 1, 1, 0),
(true, 'Zebra is a handcrafted toy made from yarn, perfect for children. Its soft texture and friendly appearance will provide hours of joy and play.', '/images/products/crochet-animals/zebra.jpg', 'Zebra - DIY Crochet Toy', 'ZEB-0003', 29.99, 50, 1, 1, 0),
(true, 'Giraffe is a handcrafted toy made from yarn, perfect for children. Its soft texture and friendly appearance will provide hours of joy and play.', '/images/products/crochet-animals/giraffe.jpg', 'Giraffe - DIY Crochet Toy', 'GIR-0004', 29.99, 50, 1, 1, 0),
(true, 'Monkey is a handcrafted toy made from yarn, perfect for children. Its soft texture and friendly appearance will provide hours of joy and play.', '/images/products/crochet-animals/monkey.jpg', 'Monkey - DIY Crochet Toy', 'MON-0005', 29.99, 50, 1, 1, 0),
(true, 'Unicorn is a handcrafted toy made from yarn, perfect for children. Its soft texture and friendly appearance will provide hours of joy and play.', '/images/products/crochet-animals/unicorn.jpg', 'Unicorn - DIY Crochet Toy', 'UNI-0006', 39.99, 50, 1, 1, 0),
(true, 'Bird is a handcrafted toy made from yarn, perfect for children. Its soft texture and friendly appearance will provide hours of joy and play.', '/images/products/crochet-animals/bird.jpg', 'Bird - DIY Crochet Toy', 'BIR-0007', 24.99, 50, 1, 1, 0),
(true, 'Reindeer is a handcrafted toy made from yarn, perfect for children. Its soft texture and friendly appearance will provide hours of joy and play.', '/images/products/crochet-animals/reindeer.jpg', 'Reindeer - DIY Crochet Toy', 'REI-0008', 34.99, 50, 1, 1, 0),
(true, 'Turtle is a handcrafted toy made from yarn, perfect for children. Its soft texture and friendly appearance will provide hours of joy and play.', '/images/products/crochet-animals/turtle.jpg', 'Turtle - DIY Crochet Toy', 'TUR-0009', 29.99, 50, 1, 1, 0),
(true, 'Fox is a handcrafted toy made from yarn, perfect for children. Its soft texture and friendly appearance will provide hours of joy and play.', '/images/products/crochet-animals/fox.jpg', 'Fox - DIY Crochet Toy', 'FOX-0010', 29.99, 50, 1, 1, 0),
(true, 'Pumpkin is a handcrafted yarn decoration, perfect for adding warmth and charm to your home decor', '/images/products/crochet-decorations/pumpkin.jpg', 'Pumpkin - DIY Crochet Decoration', 'PUM-0011', 29.99, 50, 2, 1, 0),
(true, 'Flower is a handcrafted yarn decoration, perfect for adding warmth and charm to your home decor', '/images/products/crochet-decorations/flower.jpg', 'Flower - DIY Crochet Decoration', 'FLO-0012', 9.99, 50, 2, 1, 0),
(true, 'Pillow is a handcrafted yarn decoration, perfect for adding warmth and charm to your home decor', '/images/products/crochet-decorations/pillow.jpg', 'Pillow - DIY Crochet Decoration', 'PIL-0013', 24.99, 50, 2, 1, 0),
(true, 'Blanket is a handcrafted yarn decoration, perfect for adding warmth and charm to your home decor', '/images/products/crochet-decorations/blanket.jpg', 'Blanket - DIY Crochet Decoration', 'BLA-0014', 24.99, 50, 2, 1, 0),
(true, 'Bag is a handcrafted yarn decoration, perfect for adding warmth and charm to your home decor', '/images/products/crochet-decorations/bag.jpg', 'Bag - DIY Crochet Decoration', 'BAG-0015', 29.99, 50, 2, 1, 0),
(true, 'Coaster is a handcrafted yarn decoration, perfect for adding warmth and charm to your home decor', '/images/products/crochet-decorations/coaster.jpg', 'Coaster - DIY Crochet Decoration', 'COA-0016', 9.99, 50, 2, 1, 0),
(true, 'Snowflake is a handcrafted yarn decoration, perfect for adding warmth and charm to your home decor', '/images/products/crochet-decorations/snowflake.jpg', 'Snowflake - DIY Crochet Decoration', 'SNO-0017', 9.99, 50, 2, 1, 0),
(true, 'Heart is a handcrafted yarn decoration, perfect for adding warmth and charm to your home decor', '/images/products/crochet-decorations/heart.jpg', 'Heart - DIY Crochet Decoration', 'HEA-0019', 14.99, 50, 2, 1, 0),
(true, 'Santa-Gonk Wreath is a handcrafted yarn decoration, perfect for adding warmth and charm to your home decor', '/images/products/crochet-decorations/santa-gonk-wreath.jpg', 'Santa-Gonk Wreath - DIY Crochet Decoration', 'SAN-0020', 39.99, 50, 2, 1, 0),
(true, 'Christmas Pudding Bauble is a handcrafted yarn decoration, perfect for adding warmth and charm to your home decor', '/images/products/crochet-decorations/christmas-pudding-bauble.jpg', 'Christmas Pudding Bauble - DIY Crochet Decoration', 'PUD-0020', 14.99, 50, 2, 1, 0);

