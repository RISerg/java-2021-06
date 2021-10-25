package ru.otus.solid;

import java.util.Map;

public interface ATM {
    Integer getBalance();

    Map<NoteType, Integer> take(Integer sum);

    Map<NoteType, Integer> put(Map<NoteType, Integer> notes);
}
