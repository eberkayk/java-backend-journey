package w3d1;

import java.util.Arrays;
import java.util.Objects;

public class Library {
    private final int bookCapacity;
    private final int memberCapacity;
    private Book[] books;
    private int bookCount;
    private Member[] members;
    private int memberCount;

    public Library(int bookCapacity, int memberCapacity) {
        this.bookCapacity = bookCapacity;
        this.memberCapacity = memberCapacity;
        this.books = new Book[bookCapacity];
        this.members = new Member[memberCapacity];
    }

    public void addBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book must not be null");
        }

        if (bookCount >= bookCapacity) {
            throw new IllegalStateException("Library is full");
        }

        for (int i = 0; i < bookCount; i++) {
            if (books[i].equals(book)) {
                throw new IllegalArgumentException("Duplicate ISBN: " + book.getIsbn());
            }
        }

        books[bookCount] = book;
        bookCount++;
    }

    public void addMember(Member member) {
        if (member == null) {
            throw new IllegalArgumentException("Member must not be null");
        }

        if (memberCount >= members.length) {
            throw new IllegalStateException("Member list is full");
        }

        for (int i = 0; i < memberCount; i++) {
            if (members[i].equals(member)) {
                throw new IllegalArgumentException("Duplicate member id: " + member.getId());
            }
        }

        members[memberCount] = member;
        memberCount++;
    }

    public Book findBook(String isbn) {
        for (int i = 0; i < bookCount; i++) {
            if (books[i].getIsbn().equals(isbn)) {
                return books[i];
            }
        }
        throw new BookNotFoundException(isbn);
    }

    public Member findMember(String id) {
        for (int i = 0; i < memberCount; i++) {
            if (members[i].getId().equals(id)) {
                return members[i];
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
        for (int i = 0; i < bookCount; i++) {
            if (books[i].getBorrower() == null) {
                count++;
            }
        }
        return count;
    }

    public Book[] booksByAuthor(String author) {
        int count = 0;
        for (int i = 0; i < bookCount; i++) {
            if (books[i].getAuthor().equals(author)) {
                count++;
            }
        }
        Book[] booksByAuthor = new Book[count];
        int index = 0;
        for (int i = 0; i < bookCount; i++) {
            if (books[i].getAuthor().equals(author)) {
                booksByAuthor[index] = books[i];
                index++;
            }
        }
        return booksByAuthor;
    }

    public Book[] getAllBooks() {
        Book[] result = new Book[bookCount];
        for (int i = 0; i < bookCount; i++) {
            result[i] = books[i];
        }
        return result;
    }
}

//public Book[] booksByAuthor(String author)