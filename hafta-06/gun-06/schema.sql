-- Gün 37 · Hafta 6 sınavı · hazır şema. Bu dosyayı DEĞİŞTİRME.
DROP TABLE IF EXISTS order_items, orders, products, customers;

CREATE TABLE customers (
    id        INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name      TEXT NOT NULL,
    city      TEXT,
    joined_on DATE NOT NULL
);

CREATE TABLE products (
    id       INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name     TEXT NOT NULL UNIQUE,
    category TEXT NOT NULL,
    price    NUMERIC(10,2) NOT NULL CHECK (price > 0)
);

-- customer_id NULL = misafir (üye olmadan) sipariş
CREATE TABLE orders (
    id          INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    customer_id INTEGER REFERENCES customers(id),
    ordered_on  DATE NOT NULL,
    status      TEXT NOT NULL CHECK (status IN ('PAID', 'CANCELLED'))
);

CREATE TABLE order_items (
    order_id   INTEGER NOT NULL REFERENCES orders(id),
    product_id INTEGER NOT NULL REFERENCES products(id),
    quantity   INTEGER NOT NULL CHECK (quantity > 0),
    PRIMARY KEY (order_id, product_id)
);
