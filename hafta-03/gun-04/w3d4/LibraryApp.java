package w3d4;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class LibraryApp {
    static void main(String[] args) {
        Library library = new Library();
        library.addBook(new Book("123", "Dune", "Frank Herbert"));
        library.addBook(new Book("124", "Emma", "Jane Austen"));
        library.addBook(new Book("125", "Children of Dune", "Frank Herbert"));
        library.addBook(new Book("126", "Animal Farm", "George Orwell"));
        library.addBook(new Book("127", "1984", "George Orwell"));

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
                    "6) Remove book\n" +
                    "7) Books of member\n" +
                    "8) Count by author\n" +
                    "9) List by author\n" +
                    "0) Exit");
            choice = readInt(scanner, "Choice: ");
            switch (choice) {
                case 1 -> {
                    List<Book> books = library.getAllBooks();
                    for (Book book : books) {
                        System.out.println(book);;
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
                    } catch (IllegalArgumentException e) {
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
                    List<Book> found = library.booksByAuthor(author);
                    if (found.isEmpty()) {
                        System.out.println("No books found");
                    } else {
                        System.out.println(found);
                    }
                }
                case 6 -> {
                    System.out.print("Enter ISBN: ");
                    String isbn = scanner.nextLine();
                    try {
                        library.removeBook(isbn);
                        System.out.println("Book removed");
                    } catch (LibraryException | IllegalStateException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
                case 7 -> {
                    System.out.print("Enter member id: ");
                    String id = scanner.nextLine();
                    try {
                        List<Book> borrowed = library.booksBorrowedBy(id);
                        if (borrowed.isEmpty()) {
                            System.out.println("No books borrowed");
                        } else {
                            for (Book book : borrowed) {
                                System.out.println(book);
                            }
                        }
                    } catch (LibraryException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
                case 8 -> {
                    if (library.getAllBooks().isEmpty()) {
                        System.out.println("No books");
                    } else {
                        for (Map.Entry<String, Integer> e : library.countByAuthor().entrySet()) {
                            System.out.println(e.getKey() + ": " + e.getValue());
                        }
                    }
                }
                case 9 -> {
                    if (library.getAllBooks().isEmpty()) {
                        System.out.println("No books");
                    }
                    List<Book> books = library.getAllBooks(Comparator.comparing(Book::getAuthor).thenComparing(Book::getTitle));
                    for (Book book : books) {
                        System.out.println(book);
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
