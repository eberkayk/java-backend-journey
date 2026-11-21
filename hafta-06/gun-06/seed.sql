-- Gün 37 · hazır veri. Bu dosyayı DEĞİŞTİRME. schema.sql'den hemen sonra çalışır.

INSERT INTO customers (name, city, joined_on) VALUES
    ('Ali',    'Istanbul', '2026-01-10'),   -- 1
    ('Ayse',   'Ankara',   '2026-02-03'),   -- 2
    ('Mehmet', 'Istanbul', '2026-03-15'),   -- 3
    ('Zeynep', 'Izmir',    '2026-04-20'),   -- 4
    ('Can',    NULL,       '2026-05-05'),   -- 5
    ('Elif',   'Ankara',   '2026-06-01'),   -- 6
    ('Deniz',  'Izmir',    '2026-07-12');   -- 7

INSERT INTO products (name, category, price) VALUES
    ('Laptop',   'ELECTRONICS', 12000),   -- 1
    ('Mouse',    'ELECTRONICS',   500),   -- 2
    ('Keyboard', 'ELECTRONICS',  1500),   -- 3
    ('Desk',     'HOME',         4000),   -- 4
    ('Lamp',     'HOME',          750),   -- 5
    ('Novel',    'BOOKS',         250),   -- 6
    ('Cookbook', 'BOOKS',         400);   -- 7

INSERT INTO orders (customer_id, ordered_on, status) VALUES
    (1,    '2026-09-02', 'PAID'),        -- 1
    (2,    '2026-09-05', 'PAID'),        -- 2
    (1,    '2026-09-20', 'CANCELLED'),   -- 3
    (3,    '2026-10-01', 'PAID'),        -- 4
    (NULL, '2026-10-03', 'PAID'),        -- 5  misafir
    (4,    '2026-10-15', 'PAID'),        -- 6
    (2,    '2026-10-28', 'PAID'),        -- 7
    (5,    '2026-11-02', 'CANCELLED'),   -- 8
    (1,    '2026-11-10', 'PAID'),        -- 9
    (3,    '2026-11-12', 'PAID');        -- 10

INSERT INTO order_items (order_id, product_id, quantity) VALUES
    ( 1, 1, 1), ( 1, 2, 2),
    ( 2, 6, 3),
    ( 3, 4, 1),
    ( 4, 3, 1), ( 4, 2, 1),
    ( 5, 5, 2),
    ( 6, 4, 1), ( 6, 5, 1),
    ( 7, 6, 1), ( 7, 2, 1),
    ( 8, 1, 1),
    ( 9, 3, 2),
    (10, 6, 2), (10, 5, 1);
