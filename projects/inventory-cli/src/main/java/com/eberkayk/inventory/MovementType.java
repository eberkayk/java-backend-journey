package com.eberkayk.inventory;

public enum MovementType {
    IN(1),
    OUT(-1);

    private final int sign;

    MovementType(int sign) {
        this.sign = sign;
    }

    public int signed(int qty) {
        return sign * qty;
    }
}
