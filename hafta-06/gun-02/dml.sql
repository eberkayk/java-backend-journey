-- D1
UPDATE products
SET price = price * 1.10
WHERE category = 'toys';

-- D2
UPDATE products
SET stock = 30
WHERE name = 'SQL Basics';

-- D3
UPDATE products
SET supplier = 'Generic'
WHERE supplier IS NULL;

-- D4
DELETE FROM products
WHERE discontinued = true;

-- D5
UPDATE products
SET stock = stock + 50, supplier = 'Ikea'
WHERE price < 100 AND category = 'home';

-- D6
DELETE FROM products
WHERE added_on < '2025-10-01' AND stock < 20;