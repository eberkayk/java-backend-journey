-- Gün 34 · Görev B — bu komutların HER BİRİ (biri hariç) senin kısıtların sayesinde reddedilmeli.
-- Çalıştırmadan ÖNCE her biri için tahminini chat'e yaz: geçer mi, geçmezse hangi kısıt durdurur?
-- Sonra çalıştır (psql her hatada devam eder, sonraki komuta geçer):
--   psql -U postgres -h localhost -d shop -f schema.sql -f seed.sql -f broken.sql

-- K1
INSERT INTO books (title, isbn, author_id, published_year) VALUES ('Ghost Book', 'ISBN-0099', 99, 2020);

-- K2
INSERT INTO members (full_name, email) VALUES ('Ali Yilmaz 2', 'ali@mail.com');

-- K3
INSERT INTO books (title, isbn, author_id, published_year, copies) VALUES ('Minus', 'ISBN-0098', 1, 2020, -1);

-- K4
INSERT INTO authors (country) VALUES ('France');

-- K5
INSERT INTO loans (book_id, member_id, loaned_on, returned_on) VALUES (3, 6, '2026-11-10', '2026-11-01');

-- K6
DELETE FROM authors WHERE name = 'George Orwell';

-- K7
DELETE FROM authors WHERE name = 'Yasar Kemal';

-- K8
INSERT INTO books (title, isbn, author_id, published_year) VALUES ('Kar', 'ISBN-0002', 1, 2002);
