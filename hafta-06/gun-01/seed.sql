-- Gün 32 · hazır veri (12 ürün). Bu dosyayı DEĞİŞTİRME.
-- Önce senin schema.sql'in çalışmalı (tablo onda oluşuyor):
--   psql -U postgres -d shop -f schema.sql -f seed.sql

INSERT INTO products (name, category, price, stock, discontinued, added_on, supplier) VALUES
    ('Laptop Pro 14',  'electronics', 1899.00,   5, false, '2026-03-10', 'Acme'),
    ('Wireless Mouse', 'electronics',  249.90,  40, false, '2025-11-02', 'Logi'),
    ('USB-C Cable',    'electronics',   89.50, 120, false, '2026-01-15', NULL),
    ('Old MP3 Player', 'electronics',  350.00,   3, true,  '2023-06-01', 'Acme'),
    ('Clean Code',     'books',        420.00,  12, false, '2025-09-20', 'Pearson'),
    ('SQL Basics',     'books',        180.00,   0, false, '2026-02-28', NULL),
    ('Java Pro Guide', 'books',        500.00,   7, false, '2026-05-05', 'Acme'),
    ('Lego City',      'toys',        1299.00,   4, false, '2025-12-12', 'Lego'),
    ('Puzzle 1000',    'toys',         100.00,  25, false, '2026-07-01', NULL),
    ('Yo-yo',          'toys',          35.00,  60, true,  '2024-04-04', 'Acme'),
    ('Desk Lamp',      'home',         310.00,  15, false, '2026-08-19', 'Ikea'),
    ('Coffee Mug',     'home',          75.00,  80, false, '2025-01-30', 'Ikea');
