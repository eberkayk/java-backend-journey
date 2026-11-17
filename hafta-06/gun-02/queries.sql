-- Q1
SELECT COUNT(*) AS sale_count
FROM sales;

-- Q2
SELECT COUNT(rating) AS rated, COUNT(*) - COUNT(rating) AS not_rated
FROM sales;

-- Q3
SELECT ROUND(AVG(rating), 2) AS avg_rating
FROM sales;

-- Q4
SELECT SUM(quantity * unit_price) AS revenue
FROM sales;

-- Q5
SELECT MIN(unit_price) AS cheapest, MAX(unit_price) AS most_expensive
FROM sales;

-- Q6
SELECT COUNT(DISTINCT customer) AS customer_count
FROM sales;

-- Q7
SELECT city, COUNT(*) AS sale_count
FROM sales
GROUP BY city
ORDER BY sale_count DESC, city DESC;

-- Q8
SELECT category, SUM(quantity * unit_price) AS revenue
FROM sales
GROUP BY category
ORDER BY revenue DESC;

-- Q9
SELECT customer, SUM(quantity * unit_price) AS total_spent
FROM sales
GROUP BY customer
ORDER BY total_spent DESC;

-- Q10
SELECT customer, SUM(quantity * unit_price) AS total_spent
FROM sales
GROUP BY customer
HAVING SUM(quantity * unit_price) > 2000
ORDER BY total_spent DESC;

-- Q11
SELECT category, SUM(quantity) AS units
FROM sales
WHERE sold_on < '2026-12-01' AND sold_on >= '2026-11-01'
GROUP BY category
HAVING SUM(quantity) >= 3
ORDER BY units DESC;

-- Q12
SELECT city, category, COUNT(*) AS sale_count
FROM sales
GROUP BY city, category
ORDER BY city ASC, category ASC;

-- Q13
SELECT EXTRACT(MONTH FROM sold_on) AS month, SUM(quantity * unit_price) AS revenue
FROM sales
GROUP BY EXTRACT(MONTH FROM sold_on)
ORDER BY EXTRACT(MONTH FROM sold_on) ASC;

-- Q14
SELECT product, COUNT(*) AS times_sold, ROUND(AVG(rating), 1) AS avg_rating
FROM sales
GROUP BY product
HAVING COUNT(*) >= 2
ORDER BY product ASC;

-- Q15
SELECT customer, COUNT(*) AS sale_count
FROM sales
WHERE rating IS NULL
GROUP BY customer
ORDER BY COUNT(*) DESC, customer ASC;