-- Q1
SELECT title, published_year
FROM books
WHERE published_year > (SELECT AVG(published_year) FROM books)
ORDER BY published_year ASC;

-- Q2
SELECT title, copies
FROM books
WHERE copies = (SELECT MAX(copies) FROM books);

-- Q3 En az bir kez ödünç verilmiş kitaplar — **JOIN yok** IN title title a-z
SELECT title
FROM books
WHERE id IN (SELECT book_id FROM loans)
ORDER BY title ASC;

-- Q4 Hiç ödünç almamış üyeler NOT EXISTS full_name
SELECT m.full_name
FROM members m
WHERE NOT EXISTS (SELECT 1 FROM loans l WHERE l.member_id = m.id);

-- Q5
SELECT b.title
FROM books b
WHERE id IN (SELECT book_id FROM loans WHERE returned_on IS NULL) AND author_id IN (SELECT id FROM authors WHERE country = 'Turkey')
ORDER BY title ASC;

-- Q6
SELECT b.title, (SELECT COUNT(*) FROM loans l WHERE l.book_id = b.id) AS loan_count
FROM books b
ORDER BY loan_count DESC, b.title;

-- Q7
WITH member_count AS (
    SELECT member_id, COUNT(*) AS loan_count
    FROM loans
    GROUP BY member_id
)
SELECT m.full_name, mc.loan_count
FROM member_count mc
JOIN members m ON mc.member_id = m.id
WHERE mc.loan_count = (SELECT MAX(member_count.loan_count) FROM member_count);

-- Q8
SELECT b.title, b.published_year
FROM books b
WHERE b.published_year = (SELECT MAX(b2.published_year) FROM books b2 WHERE b2.author_id = b.author_id)
ORDER BY title;

-- Q9
SELECT b.title, b.copies
FROM books b
WHERE b.copies > (SELECT AVG(b2.copies) FROM books b2 WHERE b.author_id = b2.author_id)
ORDER BY title;

-- Q10
WITH unreturned_books AS (
    SELECT book_id
    FROM loans
    WHERE returned_on IS NULL
)

SELECT DISTINCT a.name AS author
FROM unreturned_books u
JOIN books b ON b.id = u.book_id
JOIN authors a ON a.id = b.author_id
ORDER BY author;