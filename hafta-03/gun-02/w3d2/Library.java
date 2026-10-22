package w3d2;

import java.util.ArrayList;
import java.util.List;

public class Library {
    private final List<Book> books = new ArrayList<>();
    private final List<Member> members = new ArrayList<>();

    public Library() {
    }

    public void addBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book must not be null");
        }

        if (books.contains(book)) {
            throw new IllegalArgumentException("Duplicate ISBN: " + book.getIsbn());
        }


        books.add(book);
    }

    public void addMember(Member member) {
        if (member == null) {
            throw new IllegalArgumentException("Member must not be null");
        }


        if (members.contains(member)) {
            throw new IllegalArgumentException("Duplicate member id: " + member.getId());
        }


        members.add(member);
    }

    public Book findBook(String isbn) {
        for (Book book : books) {
            if (book.getIsbn().equals(isbn)) {
                return book;
            }
        }
        throw new BookNotFoundException(isbn);
    }

    public Member findMember(String id) {
        for (Member member : members) {
            if (member.getId().equals(id)) {
                return member;
            }
        }
        throw new MemberNotFoundException(id);
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
        for (Book book : books) {
            if (book.getBorrower() == null) {
                count++;
            }
        }
        return count;
    }

    public List<Book> booksByAuthor(String author) {
        List<Book> booksByAuthor = new ArrayList<>();
        for (Book book : books) {
            if (book.getAuthor().equals(author)) {
                booksByAuthor.add(book);
            }
        }
        return booksByAuthor;
    }

    public List<Book> getAllBooks() {
        List<Book> allBooks = List.copyOf(books);
        return allBooks;
    }

    public void removeBook(String isbn) {
        Book book = findBook(isbn);
        if (!book.isAvailable()) {
            throw new IllegalStateException("Book is borrowed: " + isbn);
        }
        books.remove(book);
    }

    public List<Book> booksBorrowedBy(String memberId) {
        Member member = findMember(memberId);
        List<Book> booksByMember = new ArrayList<>();
        for (Book book : books) {
            if (member.equals(book.getBorrower())) {
                booksByMember.add(book);
            }
        }
        return booksByMember;
    }
}

//public Book[] booksByAuthor(String author)