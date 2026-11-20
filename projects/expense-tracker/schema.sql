DROP TABLE IF EXISTS expenses;

CREATE TABLE expenses (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY ,
    spent_on DATE NOT NULL ,
    description TEXT NOT NULL ,
    category TEXT NOT NULL CHECK ( category IN ('FOOD', 'HOME', 'TRANSPORT') ),
    amount NUMERIC(10,2) NOT NULL CHECK ( amount > 0)
);