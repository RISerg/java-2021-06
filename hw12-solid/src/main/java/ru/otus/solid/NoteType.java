package ru.otus.solid;

public enum NoteType {
    P5000(5000), P2000(2000), P1000(1000), P500(500), P200(200), P100(100), P50(50), P10(10);
    private final int value;

    NoteType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
