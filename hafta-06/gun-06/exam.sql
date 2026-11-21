-- Gün 37 · Hafta 6 sınavı · Bölüm C
-- Çalıştırma: psql -U postgres -h localhost -d w6exam -f exam.sql
-- Her sorgunun altına yaz. Yazamadığını boş bırak.

-- Q1
SELECT name, city
FROM customers
WHERE city = 'Ankara' OR city = 'Istanbul'
ORDER BY name;

-- Q2
SELECT name
FROM customers
WHERE city IS NULL;

-- Q3
SELECT category, COUNT(*) AS product_count, ROUND(AVG(price), 2) AS avg_price
FROM products
GROUP BY category
ORDER BY product_count DESC, category;

-- Q4
SELECT  o2.id, o2.ordered_on, p.name, o.quantity, p.price * o.quantity AS price
FROM order_items o
LEFT JOIN orders o2 ON o.order_id = o2.id
LEFT JOIN products p ON p.id = o.product_id
WHERE ordered_on < '2026-11-01' AND ordered_on >= '2026-10-01'
ORDER BY o2.id, p.name;

-- Q5
SELECT c.name, COUNT(o.status) AS paid_count
FROM customers c
LEFT JOIN orders o ON o.customer_id = c.id AND o.status = 'PAID'
GROUP BY c.name
ORDER BY paid_count DESC, name;

-- Q6
SELECT c.name
FROM customers c
LEFT JOIN orders o ON o.customer_id = c.id
WHERE o.id IS NULL
ORDER BY c.name;

-- Q7
SELECT p.name, SUM(o.quantity) AS total_amount
FROM products p
JOIN order_items o ON p.id = o.product_id
JOIN orders o2 ON o2.id = o.order_id
WHERE o2.status = 'PAID'
GROUP BY p.name
HAVING SUM(o.quantity) >= 3
ORDER BY total_amount DESC, p.name;


-- Q8
WITH paid AS (
    SELECT o.order_id, SUM(o.quantity * p.price) AS total
    FROM order_items o
    JOIN products p ON o.product_id = p.id
    JOIN orders o2 ON o2.id = o.order_id
    WHERE o2.status = 'PAID'
    GROUP BY o.order_id
)

SELECT p2.order_id, p2.total
FROM paid p2
WHERE p2.total > (SELECT AVG(total) FROM paid)
ORDER BY p2.total DESC;

-- Q9
SELECT c.name
FROM customers c
WHERE EXISTS (SELECT 1 FROM orders o
              WHERE o.customer_id = c.id AND o.status = 'PAID'
                AND o.ordered_on < '2026-11-01')
  AND NOT EXISTS (SELECT 1 FROM orders o
                  WHERE o.customer_id = c.id AND o.status = 'PAID'
                    AND o.ordered_on >= '2026-11-01' AND o.ordered_on < '2026-12-01')
ORDER BY c.name;

-- Q10
WITH sales AS (
    SELECT o.product_id, SUM(p.price*o.quantity) AS revenue
    FROM order_items o
    JOIN orders o2 ON o2.id = o.order_id
    JOIN products p ON p.id = o.product_id
    WHERE o2.status = 'PAID'
    GROUP BY o.product_id
)

SELECT p.name, COALESCE(s.revenue, 0) AS revenue
FROM products p
LEFT JOIN sales s ON s.product_id = p.id
ORDER BY revenue DESC, p.name;