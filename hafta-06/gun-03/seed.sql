-- Gün 34 · hazır veri. Bu dosyayı DEĞİŞTİRME.
-- Önce senin schema.sql'in çalışmalı (tablolar boş ve id'ler 1'den başlıyor olmalı):
--   psql -U postgres -h localhost -d shop -f schema.sql -f seed.sql
-- id'ler eklenme sırasına göre 1, 2, 3... olur; loans tablosu bu numaraları kullanır.

INSERT INTO authors (name, country) VALUES
    ('Orhan Pamuk',       'Turkey'),   -- 1
    ('Sabahattin Ali',    'Turkey'),   -- 2
    ('George Orwell',     'UK'),       -- 3
    ('Fyodor Dostoevsky', 'Russia'),   -- 4
    ('Elif Shafak',       'Turkey'),   -- 5
    ('Yasar Kemal',       'Turkey'),   -- 6  (kitabı yok)
    ('Jane Doe',          NULL);       -- 7  (kitabı yok, ülkesi bilinmiyor)

INSERT INTO books (title, isbn, author_id, published_year, copies) VALUES
    ('Benim Adim Kirmizi',   'ISBN-0001', 1, 1998, 2),   -- 1
    ('Kar',                  'ISBN-0002', 1, 2002, 1),   -- 2
    ('Masumiyet Muzesi',     'ISBN-0003', 1, 2008, 1),   -- 3
    ('Kuyucakli Yusuf',      'ISBN-0004', 2, 1937, 2),   -- 4
    ('Icimizdeki Seytan',    'ISBN-0005', 2, 1940, 1),   -- 5
    ('1984',                 'ISBN-0006', 3, 1949, 3),   -- 6
    ('Animal Farm',          'ISBN-0007', 3, 1945, 2),   -- 7
    ('Crime and Punishment', 'ISBN-0008', 4, 1866, 1),   -- 8
    ('The Idiot',            'ISBN-0009', 4, 1869, 1),   -- 9
    ('Ask',                  'ISBN-0010', 5, 2009, 2);   -- 10

INSERT INTO members (full_name, email, joined_on) VALUES
    ('Ali Yilmaz',   'ali@mail.com',    '2026-09-01'),   -- 1
    ('Ayse Kaya',    'ayse@mail.com',   '2026-09-05'),   -- 2
    ('Mehmet Demir', 'mehmet@mail.com', '2026-09-10'),   -- 3
    ('Zeynep Celik', 'zeynep@mail.com', '2026-10-01'),   -- 4
    ('Can Ozturk',   'can@mail.com',    '2026-10-15'),   -- 5
    ('Elif Arslan',  'elif@mail.com',   '2026-11-01');   -- 6  (hiç ödünç almadı)

-- returned_on NULL = kitap hâlâ üyede
INSERT INTO loans (book_id, member_id, loaned_on, returned_on) VALUES
    ( 6, 1, '2026-09-03', '2026-09-12'),
    ( 1, 1, '2026-09-15', '2026-10-10'),
    ( 4, 2, '2026-09-06', '2026-09-18'),
    ( 6, 2, '2026-09-20', '2026-09-30'),
    ( 8, 3, '2026-09-12', '2026-10-20'),
    ( 7, 3, '2026-10-02', '2026-10-09'),
    (10, 4, '2026-10-03', '2026-10-15'),
    ( 2, 4, '2026-10-20', NULL),
    ( 6, 5, '2026-10-16', '2026-10-29'),
    ( 1, 2, '2026-11-02', NULL),
    ( 4, 1, '2026-11-05', '2026-11-12'),
    (10, 3, '2026-11-08', NULL),
    ( 7, 1, '2026-11-10', NULL);
