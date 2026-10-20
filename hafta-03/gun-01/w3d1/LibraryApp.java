package w3d1;

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
            choice = readInt(scanner, "Choice: ");
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
                    try {
                        library.addBook(new Book(isbn, title, author));
                        System.out.println("Book added");
                    } catch (IllegalArgumentException | IllegalStateException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
                case 3 -> {
                    System.out.print("Enter member id: ");
                    String id = scanner.nextLine();
                    System.out.print("Enter ISBN: ");
                    String isbn = scanner.nextLine();
                    try {
                        library.borrowBook(id, isbn);
                        System.out.println("Book borrowed by member: " + id);
                    } catch (LibraryException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
                case 4 -> {
                    System.out.print("Enter member id: ");
                    String id = scanner.nextLine();
                    System.out.print("Enter ISBN: ");
                    String isbn = scanner.nextLine();
                    try {
                        library.returnBook(id, isbn);
                        System.out.println("Book returned");
                    } catch (LibraryException e) {
                        System.out.println("Error: " + e.getMessage());
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

    private static int readInt(Scanner scanner, String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                int result = Integer.parseInt(scanner.nextLine());
                return result;
            } catch (NumberFormatException e) {
                System.out.print("Please enter a integer value: ");
            }
        }
    }
}
