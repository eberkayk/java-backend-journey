BEGIN;
UPDATE loans SET returned_on = '2026-11-19' WHERE book_id = 2 AND member_id = 4 AND returned_on IS NULL;
UPDATE books SET copies = copies + 1 WHERE id = 2 ;
COMMIT;