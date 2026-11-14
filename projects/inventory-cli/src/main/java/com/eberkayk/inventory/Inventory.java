package com.eberkayk.inventory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Inventory {
    private final List<Movement> movements = new ArrayList<>();

    public void add(Movement m) throws InsufficientStockException {
        if (m == null) {
            throw new IllegalArgumentException("Movement is null");
        }
        if (m.type() == MovementType.OUT && stockOf(m.sku()) < m.qty()) {
            throw new InsufficientStockException(m.sku(), stockOf(m.sku()), m.qty());
        }
        movements.add(m);
    }

    public int stockOf(String sku) {
        return movements.stream().filter(n -> Objects.equals(n.sku(), sku)).mapToInt(Movement::signedQty).sum();
    }

    public Map<String , Integer> stockAll() {
        return new TreeMap<>(movements.stream().collect(Collectors.groupingBy(Movement::sku, Collectors.summingInt(Movement::signedQty))));
    }

    public List<Movement> history(String sku) {
        return new ArrayList<>(movements.stream().filter(n -> Objects.equals(n.sku(), sku)).sorted(Comparator.comparing(Movement::date)).toList());
    }

    public List<String> lowStock(int threshold) {
        return movements.stream().map(Movement::sku).filter(sku -> stockOf(sku) < threshold).distinct().sorted().toList();
    }

    public Optional<String> topSeller() {
        Map<String, Integer> map = new TreeMap<>(movements.stream().filter(n -> n.type() == MovementType.OUT).collect(Collectors.groupingBy(Movement::sku, Collectors.summingInt(Movement::qty))));

        if (map.isEmpty()) {
            return Optional.empty();
        }

        String bestSku = null;
        int bestTotal = 0;
        for (Map.Entry<String, Integer> e : map.entrySet()) {
            if (e.getValue() > bestTotal) {
                bestSku = e.getKey();
                bestTotal = e.getValue();
            }
        }
        return Optional.of(bestSku);
    }

    public int totalOut(LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("Bad range: " + from + " > " + to);
        }

        return movements.stream().filter(n -> !n.date().isBefore(from)).filter(n -> !n.date().isAfter(to))
                .filter(n -> n.type() == MovementType.OUT).mapToInt(Movement::qty).sum();
    }

    public List<Movement> all() {
        return List.copyOf(movements);
    }

    public void saveTo(Path file) throws IOException {
        List<String> list = movements.stream().map(Movement::toCsv).toList();
        Files.write(file, list);
    }

    public static Inventory loadFrom(Path file) throws IOException {
        if (!Files.exists(file)) {
            return new Inventory();
        }
        Inventory inventory = new Inventory();
        List<String> list = Files.readAllLines(file);
        for (String s : list) {
            if (s.isBlank()) {
                continue;
            }
            inventory.movements.add(Movement.fromCsv(s));
        }
        return inventory;
    }
}
