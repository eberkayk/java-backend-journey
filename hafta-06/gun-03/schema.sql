DROP TABLE IF EXISTS loans, books, members, authors;

CREATE TABLE authors (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name TEXT NOT NULL ,
    country TEXT
);

CREATE TABLE members (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY ,
    full_name TEXT NOT NULL ,
    email TEXT NOT NULL UNIQUE ,
    joined_on DATE NOT NULL DEFAULT current_date
);

CREATE TABLE books (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title TEXT NOT NULL,
    isbn TEXT NOT NULL UNIQUE ,
    author_id INTEGER NOT NULL REFERENCES authors(id),
    published_year INTEGER NOT NULL CHECK ( published_year <= 2100 AND published_year >= 1450 ),
    copies INTEGER NOT NULL DEFAULT 1 CHECK ( copies >= 0 )
);

CREATE TABLE loans (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY ,
    book_id INTEGER NOT NULL REFERENCES books(id),
    member_id INTEGER NOT NULL REFERENCES members(id),
    loaned_on DATE NOT NULL ,
    returned_on DATE ,
    CHECK ( returned_on >= loaned_on )
);