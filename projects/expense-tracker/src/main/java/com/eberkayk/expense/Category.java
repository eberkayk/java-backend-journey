package com.eberkayk.expense;

public enum Category {
    FOOD("Food"),
    HOME("Home"),
    TRANSPORT("Transport");

    private final String label;
    Category(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
