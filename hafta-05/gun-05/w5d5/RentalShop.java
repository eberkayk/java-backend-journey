package w5d5;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class RentalShop {
    private final List<Rental> rentals = new ArrayList<>();

    public void add(Rental rental) {
        if (rental == null) {
            throw new IllegalArgumentException("Rental must not be null");
        }
        if (rentals.stream().map(Rental::id).anyMatch(n -> Objects.equals(n, rental.id()))) {
            throw new IllegalStateException("Duplicate id: " + rental.id());
        }
        rentals.add(rental);
    }

    public Optional<Rental> findById(String id) {
        return rentals.stream().filter(n -> Objects.equals(n.id(), id)).findFirst();
    }

    public long priceOf(String id) {
        if (findById(id).isEmpty()) {
            throw new NoSuchElementException("No rental: " + id);
        }
        return rentals.stream().filter(n -> Objects.equals(n.id(), id)).findFirst().stream().mapToLong(Rental::price).sum();
    }

    public List<Rental> activeOn(LocalDate day) {
        return new ArrayList<>(rentals.stream().filter(n -> !n.start().isAfter(day)).filter(n -> !n.end().isBefore(day)).toList());
    }

    public Map<BikeType, Long> revenueByType() {
        return new TreeMap<>(rentals.stream().collect(Collectors.groupingBy(Rental::type, Collectors.summingLong(Rental::price))));
    }

    public Optional<Rental> longest() {
        return rentals.stream().max(Comparator.comparingLong(Rental::days));
    }

    public void saveTo(Path path) throws IOException {
        List<String> rentals1 = new ArrayList<>();
        for (Rental r : rentals) {
            rentals1.add(r.toCsv());
        }
        Files.write(path, rentals1);
    }

    public static RentalShop loadFrom(Path path) throws IOException {
        RentalShop rentalShop = new RentalShop();
        List<String> lines = Files.readAllLines(path);
        for (String s : lines) {
            if (s.isBlank()) {
                continue;
            }
            rentalShop.add(Rental.fromCsv(s));
        }
        return rentalShop;
    }
}
