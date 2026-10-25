package w3d5;

import java.util.*;

public class Library implements Iterable<Book> {
    private final Map<String, Book> books = new HashMap<>();
    private final Map<String, Member> members = new HashMap<>();

    public Library() {
    }

    public void addBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book must not be null");
        }

        if (books.containsKey(book.getIsbn())) {
            throw new IllegalArgumentException("Duplicate ISBN: " + book.getIsbn());
        }


        books.put(book.getIsbn(), book);
    }

    public void addMember(Member member) {
        if (member == null) {
            throw new IllegalArgumentException("Member must not be null");
        }


        if (members.containsKey(member.getId())) {
            throw new IllegalArgumentException("Duplicate member id: " + member.getId());
        }


        members.put(member.getId(), member);
    }

    public Book findBook(String isbn) {
        Book book = books.get(isbn);
        if (book == null) {
            throw new BookNotFoundException(isbn);
        }
        return book;
    }

    public Member findMember(String id) {
        Member member = members.get(id);
        if (member == null) {
            throw new MemberNotFoundException(id);
        }
        return member;
    }

    public void borrowBook(String memberId, String isbn) {
        Member member = findMember(memberId);

        Book book = findBook(isbn);

        if (!book.isAvailable()) {
            throw new BookNotAvailableException(isbn);
        }

        if (!member.canBorrow()) {
            throw new BorrowLimitExceededException(member);
        }

        book.setBorrower(member);
        member.onBorrow();
    }

    public void returnBook(String memberId, String isbn) {
        Member member = findMember(memberId);
        Book book = findBook(isbn);
        if (!member.equals(book.getBorrower())) {
            throw new NotBorrowedByMemberException(memberId, isbn);
        }

        book.setBorrower(null);
        member.onReturn();
    }

    public int availableCount() {
        int count = 0;
        for (Book book : books.values()) {
            if (book.isAvailable()) {
                count++;
            }
        }
        return count;
    }

    public List<Book> booksByAuthor(String author) {
        List<Book> booksByAuthor = new ArrayList<>();
        for (Book book : books.values()) {
            if (book.getAuthor().equals(author)) {
                booksByAuthor.add(book);
            }
        }
        booksByAuthor.sort(Comparator.comparing(Book::getTitle));
        return booksByAuthor;
    }

    public List<Book> getAllBooks() {
        List<Book> allBooks = new ArrayList<>(books.values());
        allBooks.sort(Book::compareTo);
        return List.copyOf(allBooks);
    }

    public List<Book> getAllBooks(Comparator<Book> order) {
        if (order == null) {
            throw new IllegalArgumentException("Order must not be null");
        }
        List<Book> allBooks = new ArrayList<>(books.values());
        allBooks.sort(order);
        return List.copyOf(allBooks);
    }

    public void removeBook(String isbn) {
        Book book = findBook(isbn);
        if (!book.isAvailable()) {
            throw new IllegalStateException("Book is borrowed: " + isbn);
        }
        books.remove(isbn);
    }

    public List<Book> booksBorrowedBy(String memberId) {
        Member member = findMember(memberId);
        List<Book> booksByMember = new ArrayList<>();
        for (Book book : books.values()) {
            if (member.equals(book.getBorrower())) {
                booksByMember.add(book);
            }
        }
        return booksByMember;
    }

    public Map<String, Integer> countByAuthor() {
        Map<String, Integer> result = new HashMap<>();
        for (Book book : books.values()) {
            result.put(book.getAuthor(), result.getOrDefault(book.getAuthor(), 0) + 1);
        }
        return Map.copyOf(result);
    }

    public Set<String> authors() {
        Set<String> authors = new HashSet<>();
        for (Book book : books.values()) {
            authors.add(book.getAuthor());
        }
        return Set.copyOf(authors);
    }

    public int removeBooksByAuthor(String author) {
        if (author == null || author.isBlank()) {
            throw new IllegalArgumentException("Author must not be blank");
        }
        int count = 0;
        Iterator<Book> iterator = books.values().iterator();
        while (iterator.hasNext()) {
            Book nextBook = iterator.next();
            if (nextBook.getAuthor().equals(author) && nextBook.isAvailable()) {
                iterator.remove();
                count++;
            }
        }
        return count;
    }

    @Override
    public Iterator<Book> iterator() {
        return getAllBooks().iterator();
    }
}

//public Book[] booksByAuthor(String author)