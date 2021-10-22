package ru.otus.solid;

import java.util.Map;

public interface Cell {
    Integer getBalance();
    Map.Entry<NoteType, Integer> take(Integer count);
    Integer put(Integer count);
}
