DROP TABLE IF EXISTS products;

CREATE TABLE products (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name TEXT NOT NULL,
    category TEXT NOT NULL,
    price NUMERIC(10, 2) NOT NULL,
    stock INTEGER NOT NULL,
    discontinued BOOLEAN NOT NULL DEFAULT false,
    added_on DATE NOT NULL,
    supplier TEXT
);

INSERT INTO products (name, category, price, stock, added_on, supplier) VALUES
                                                                            ('Gaming Headset PRO', 'electronics', 999.00, 9, '2026-11-01', 'Logi'),
                                                                            ('Notebook', 'home', 45.00, 200, '2026-11-16', NULL);