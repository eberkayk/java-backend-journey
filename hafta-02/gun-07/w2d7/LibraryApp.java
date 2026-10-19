package w2d7;

import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.Scanner;

public class LibraryApp {
    static void main(String[] args) {
        Library library = new Library(10, 10);
        library.addBook(new Book("123", "title1", "author1"));
        library.addBook(new Book("124", "title2", "author2"));
        library.addBook(new Book("125", "title3", "author3"));
        library.addBook(new Book("126", "title4", "author4"));
        library.addBook(new Book("127", "title5", "author5"));

        library.addMember(new StudentMember("12", "x"));
        library.addMember(new StudentMember("13", "y"));
        library.addMember(new StaffMember("14", "z"));
        int choice = -1;
        Scanner scanner = new Scanner(System.in);
        while (choice != 0) {
            System.out.println("=== LIBRARY ===\n" +
                    "1) List all books\n" +
                    "2) Add book\n" +
                    "3) Borrow book\n" +
                    "4) Return book\n" +
                    "5) Search by author\n" +
                    "0) Exit");
            choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1 -> {
                    Book[] books = library.getAllBooks();
                    for (int i = 0; i < books.length; i++) {
                        System.out.println(books[i]);
                    }
                }
                case 2 -> {
                    System.out.print("Enter ISBN: ");
                    String isbn = scanner.nextLine();
                    System.out.print("Enter title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter author: ");
                    String author = scanner.nextLine();
                    if (library.addBook(new Book(isbn, title, author))) {
                        System.out.println("Book added");
                    } else {
                        System.out.println("Add failed");
                    }
                }
                case 3 -> {
                    System.out.print("Enter member id: ");
                    String id = scanner.nextLine();
                    System.out.print("Enter ISBN: ");
                    String isbn = scanner.nextLine();
                    if (library.borrowBook(id, isbn)) {
                        System.out.println("Borrowed");
                    } else {
                        System.out.println("Borrow failed");
                    }
                }
                case 4 -> {
                    System.out.print("Enter member id: ");
                    String id = scanner.nextLine();
                    System.out.print("Enter ISBN: ");
                    String isbn = scanner.nextLine();
                    if (library.returnBook(id, isbn)) {
                        System.out.println("Returned");
                    } else {
                        System.out.println("Return failed");
                    }
                }
                case 5 -> {
                    System.out.print("Enter author: ");
                    String author = scanner.nextLine();
                    Book[] found = library.booksByAuthor(author);
                    if (found.length == 0) {
                        System.out.println("No books found");
                    } else {
                        for (int i = 0; i < found.length; i++) {
                            System.out.println(found[i]);
                        }
                    }
                }
                case 0 -> System.out.println("Bye");
                default -> System.out.println("Invalid choice");
            }
        }
    }
}
