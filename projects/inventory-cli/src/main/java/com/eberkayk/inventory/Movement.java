package com.eberkayk.inventory;

import java.time.LocalDate;

public record Movement(String sku, MovementType type, int qty, LocalDate date) {

    public Movement {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("SKU is blank");
        }

        if (type == null) {
            throw new IllegalArgumentException("Type is null");
        }

        if (qty <= 0) {
            throw new IllegalArgumentException("Quantity must be positive: " + qty);
        }

        if (date == null) {
            throw new IllegalArgumentException("Date is null");
        }
    }

    public int signedQty() {
        return type.signed(qty);
    }

    public String toCsv() {
        return sku + ";" + type + ";" + qty + ";" + date;
    }

    public static Movement fromCsv(String line) {
        String[] strings = line.split(";");
        if (strings.length != 4) {
            throw new IllegalArgumentException("Bad line: " + line);
        }
        String sku1 = strings[0];
        MovementType type1 = MovementType.valueOf(strings[1]);
        int qty1 = Integer.parseInt(strings[2]);
        LocalDate date1 = LocalDate.parse(strings[3]);

        return new Movement(sku1, type1, qty1, date1);

    }


}
