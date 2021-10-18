package ru.otus.solid;

import java.util.Objects;

public class NoteType implements Comparable {
    private int value;

    public NoteType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NoteType noteType = (NoteType) o;
        return value == noteType.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public int compareTo(Object o) {
        if (o == null || getClass() != o.getClass())
            throw new IllegalArgumentException();
        return (int) Math.signum(getValue() - ((NoteType) o).getValue());
    }
}
