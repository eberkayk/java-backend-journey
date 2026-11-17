-- Gün 33 · hazır tablo + veri (20 satış). Bu dosyayı DEĞİŞTİRME.
--   psql -U postgres -h localhost -d shop -f sales.sql

DROP TABLE IF EXISTS sales;

CREATE TABLE sales (
    id         INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    sold_on    DATE           NOT NULL,
    customer   TEXT           NOT NULL,
    city       TEXT           NOT NULL,
    product    TEXT           NOT NULL,
    category   TEXT           NOT NULL,
    quantity   INTEGER        NOT NULL,
    unit_price NUMERIC(10, 2) NOT NULL,
    rating     INTEGER                     -- 1-5, müşteri puan vermediyse NULL
);

INSERT INTO sales (sold_on, customer, city, product, category, quantity, unit_price, rating) VALUES
    ('2026-09-03', 'Ali',    'Istanbul', 'Wireless Mouse',     'electronics',  2,  249.90, 5),
    ('2026-09-05', 'Ayse',   'Ankara',   'Clean Code',         'books',        1,  420.00, 4),
    ('2026-09-12', 'Ali',    'Istanbul', 'USB-C Cable',        'electronics',  3,   89.50, NULL),
    ('2026-09-20', 'Mehmet', 'Izmir',    'Lego City',          'toys',         1, 1299.00, 5),
    ('2026-09-28', 'Zeynep', 'Istanbul', 'Coffee Mug',         'home',         4,   75.00, 3),
    ('2026-10-02', 'Can',    'Ankara',   'Laptop Pro 14',      'electronics',  1, 1899.00, 4),
    ('2026-10-02', 'Ayse',   'Ankara',   'SQL Basics',         'books',        2,  180.00, NULL),
    ('2026-10-09', 'Elif',   'Izmir',    'Desk Lamp',          'home',         1,  310.00, 2),
    ('2026-10-15', 'Ali',    'Istanbul', 'Java Pro Guide',     'books',        1,  500.00, 5),
    ('2026-10-15', 'Mehmet', 'Izmir',    'Puzzle 1000',        'toys',         2,  100.00, NULL),
    ('2026-10-21', 'Zeynep', 'Istanbul', 'Wireless Mouse',     'electronics',  1,  249.90, 4),
    ('2026-10-30', 'Can',    'Ankara',   'USB-C Cable',        'electronics',  5,   89.50, 3),
    ('2026-11-01', 'Ayse',   'Ankara',   'Gaming Headset PRO', 'electronics',  1,  999.00, 5),
    ('2026-11-03', 'Elif',   'Izmir',    'Coffee Mug',         'home',         6,   75.00, NULL),
    ('2026-11-07', 'Ali',    'Istanbul', 'Lego City',          'toys',         1, 1299.00, 4),
    ('2026-11-10', 'Zeynep', 'Istanbul', 'Clean Code',         'books',        1,  420.00, NULL),
    ('2026-11-12', 'Mehmet', 'Izmir',    'Laptop Pro 14',      'electronics',  1, 1899.00, 3),
    ('2026-11-14', 'Can',    'Ankara',   'Notebook',           'home',        10,   45.00, 5),
    ('2026-11-15', 'Elif',   'Izmir',    'Java Pro Guide',     'books',        2,  500.00, 4),
    ('2026-11-16', 'Ali',    'Istanbul', 'Notebook',           'home',         3,   45.00, NULL);
