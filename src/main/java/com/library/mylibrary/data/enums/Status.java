package com.library.mylibrary.data.enums;

public enum Status {
    AVAILABLE, BORROWED;

    public static Status fromString(String text) {
        return switch (text) {
            case "Dostępny" -> AVAILABLE;
            case "Wypożyczony" -> BORROWED;
            default -> throw new IllegalArgumentException("Nieznany status: " + text);
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case AVAILABLE -> "Dostępny";
            case BORROWED -> "Wypożyczony";
        };
    }
}
