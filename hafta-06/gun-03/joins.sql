-- J1
SELECT b.title, a.name AS author
FROM books b
LEFT JOIN authors a ON b.author_id = a.id
ORDER BY b.title ASC;

-- J2
SELECT b.title, b.published_year
FROM books b
LEFT JOIN authors a ON b.author_id = a.id
WHERE a.country = 'Turkey'
ORDER BY b.published_year ASC;

-- J3
SELECT a.name AS author, COUNT(b.id) AS book_count
FROM authors a
LEFT JOIN books b ON a.id = b.author_id
GROUP BY a.name
ORDER BY COUNT(b.id) DESC , a.name;

-- J4
SELECT a.name AS author
FROM authors a
LEFT JOIN books b ON b.author_id = a.id
WHERE b.author_id IS NULL
ORDER BY a.name;

-- J5
SELECT m.full_name AS member, b.title AS title, loaned_on
FROM loans l
LEFT JOIN books b ON b.id = l.book_id
LEFT JOIN members m ON m.id = l.member_id
ORDER BY loaned_on ASC;

-- J6
SELECT m.full_name AS member, b.title AS title, loaned_on
FROM loans l
LEFT JOIN books b ON b.id = l.book_id
LEFT JOIN members m ON m.id = l.member_id
WHERE l.returned_on IS NULL
ORDER BY loaned_on ASC;

-- J7
SELECT m.full_name AS member
FROM members m
LEFT JOIN loans l ON l.member_id = m.id
WHERE l.member_id IS NULL ;

-- J8
SELECT b.title
FROM books b
LEFT JOIN loans l ON l.book_id = b.id
WHERE l.book_id IS NULL ;

-- J9
SELECT m.full_name AS member, COUNT(l.member_id) AS loan_count
FROM members m
LEFT JOIN loans l ON l.member_id = m.id
GROUP BY m.full_name
HAVING COUNT(l.member_id) >= 3
ORDER BY COUNT(l.member_id) DESC, m.full_name;

-- J10
SELECT a.name AS author, COUNT(l.id) AS times_loaned
FROM authors a
LEFT JOIN books b ON b.author_id = a.id
LEFT JOIN loans l ON l.book_id = b.id
WHERE l.id IS NOT NULL
GROUP BY a.name
ORDER BY COUNT(l.id) DESC , a.name ;

-- J11
SELECT m.full_name AS member, b.title, l.returned_on - l.loaned_on AS days_kept
FROM members m
LEFT JOIN loans l ON l.member_id = m.id
LEFT JOIN books b ON b.id = l.book_id
WHERE l.returned_on - l.loaned_on > 14
ORDER BY l.returned_on - l.loaned_on DESC ;

-- J12
SELECT m.full_name AS member, COUNT(l.loaned_on) AS november_loans
FROM members m
LEFT JOIN loans l ON l.member_id = m.id AND l.loaned_on < '2026-12-01' AND l.loaned_on >= '2026-11-01'
GROUP BY m.full_name
ORDER BY november_loans DESC, member;