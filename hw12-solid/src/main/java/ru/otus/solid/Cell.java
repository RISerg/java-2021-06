package ru.otus.solid;

import java.util.Map;

public interface Cell {
    NoteType getNoteType();

    Integer getBalance();

    Boolean hasSomeNotes(Integer count);

    Map.Entry<NoteType, Integer> take(Integer count);

    Integer put(Integer count);
}
