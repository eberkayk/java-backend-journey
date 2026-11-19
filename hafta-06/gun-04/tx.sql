-- Gün 35 · Görev B · hazır script. Bu dosyayı DEĞİŞTİRME.
-- Önce temiz veri:  psql -U postgres -h localhost -d shop -f ../gun-03/schema.sql -f ../gun-03/seed.sql
-- Başlangıç: The Idiot (id 9) copies = 1, loans tablosunda 13 satır.

-- T1: Elif (üye 6) The Idiot'u ödünç alıyor
BEGIN;
UPDATE books SET copies = copies - 1 WHERE id = 9;
INSERT INTO loans (book_id, member_id, loaned_on) VALUES (9, 6, '2026-11-19');
COMMIT;

-- T2: Can (üye 5) da The Idiot'u ödünç almaya çalışıyor
BEGIN;
UPDATE books SET copies = copies - 1 WHERE id = 9;
INSERT INTO loans (book_id, member_id, loaned_on) VALUES (9, 5, '2026-11-19');
COMMIT;

-- T3: Ali'nin (üye 1) bütün ödünç kayıtları siliniyor... ya da değil
BEGIN;
DELETE FROM loans WHERE member_id = 1;
ROLLBACK;

-- T4: T2'nin aynısı, ama BEGIN/COMMIT YOK
UPDATE books SET copies = copies - 1 WHERE id = 9;
INSERT INTO loans (book_id, member_id, loaned_on) VALUES (9, 5, '2026-11-19');

-- Sonuç
SELECT copies FROM books WHERE id = 9;
SELECT COUNT(*) AS loan_count FROM loans;
SELECT member_id, loaned_on FROM loans WHERE book_id = 9 ORDER BY member_id;
