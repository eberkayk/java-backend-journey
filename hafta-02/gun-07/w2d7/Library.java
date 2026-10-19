package w2d7;

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

    public boolean addBook(Book book) {
        if (book == null || bookCount >= books.length) {
            return false;
        }
        for (int i = 0; i < bookCount; i++) {
            if (books[i].equals(book)) {
                return false;
            }
        }
        books[bookCount] = book;
        bookCount++;
        return true;
    }

    public boolean addMember(Member member) {
        if (member == null || memberCount >= members.length) {
            return false;
        }
        for (int i = 0; i < memberCount; i++) {
            if (members[i].equals(member)) {
                return false;
            }
        }
        members[memberCount] = member;
        memberCount++;
        return true;
    }

    public Book findBook(String isbn) {
        for (int i = 0; i < bookCount; i++) {
            if (books[i].getIsbn().equals(isbn)) {
                return books[i];
            }
        }
        return null;
    }

    public Member findMember(String id) {
        for (int i = 0; i < memberCount; i++) {
            if (members[i].getId().equals(id)) {
                return members[i];
            }
        }
        return null;
    }
    public boolean borrowBook(String memberId, String isbn) {
        if(findMember(memberId) == null || findBook(isbn) == null || !findBook(isbn).isAvailable() || !findMember(memberId).canBorrow()) {
            return false;
        } else {
            findBook(isbn).setBorrower(findMember(memberId));
            findMember(memberId).onBorrow();
            return true;
        }
    }

    public boolean returnBook(String memberId, String isbn) {
        if (findMember(memberId) == null || findBook(isbn) == null || !findMember(memberId).equals(findBook(isbn).getBorrower())) {
            return false;
        } else {
            findBook(isbn).setBorrower(null);
            findMember(memberId).onReturn();
            return true;
        }
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