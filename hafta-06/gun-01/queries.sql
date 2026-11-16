-- Q1
SELECT name, price
FROM products
ORDER BY price DESC;

-- O2
SELECT name, price
FROM products
WHERE category = 'electronics' AND discontinued = false
ORDER BY name ASC;

-- Q3
SELECT name, price
FROM products
WHERE price BETWEEN 100 AND 500
ORDER BY price ASC;

-- Q4
SELECT name
FROM products
WHERE name ILIKE '%pro%'
ORDER BY name ASC;

-- Q5
SELECT name, category
FROM products
WHERE category IN ('books', 'toys')
ORDER BY category ASC, price DESC;

-- Q6
SELECT name
FROM products
WHERE supplier IS NULL
ORDER BY name ASC;

-- Q7
SELECT name, supplier
FROM products
WHERE supplier <> 'Acme' OR supplier IS NULL
ORDER BY name ASC;

-- Q8
SELECT DISTINCT category
FROM products
ORDER BY category;

--Q9
SELECT name, price
FROM products
ORDER BY price DESC
LIMIT 3;

-- Q10
SELECT name, price*stock AS stock_value
FROM products
WHERE price*stock > 5000
ORDER BY stock_value DESC;

-- Q11
SELECT name, added_on
FROM products
WHERE added_on BETWEEN '2026-01-01' AND '2026-12-31'
ORDER BY added_on ASC;

-- Q12
SELECT name, COALESCE(supplier, 'unknown') AS supplier
FROM products
ORDER BY name ASC;

-- Q13
SELECT name, price
FROM products
ORDER BY price ASC
LIMIT 4
OFFSET 4;