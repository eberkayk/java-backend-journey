package com.eberkayk.inventory;

public class InsufficientStockException extends Exception {
    public InsufficientStockException(String sku, int have, int need) {
        super("Not enough stock for " + sku + ": have " + have + ", need " + need);
    }
}
