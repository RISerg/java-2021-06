package ru.otus.solid;

import java.util.HashMap;
import java.util.Map;

public class SimpleCell implements Cell {
    private final NoteType noteType;
    private Integer noteCount;

    public SimpleCell(NoteType noteType) {
        this.noteType = noteType;
        this.noteCount = 0;
    }

    public NoteType getNoteType() {
        return this.noteType;
    }

    public Integer getBalance() {
        return noteCount * noteType.getValue();
    }

    public Boolean hasSomeNotes(Integer count) {
        return noteCount >= count;
    }

    public Map.Entry<NoteType, Integer> take(Integer count) {
        if (count > noteCount)
            throw new RuntimeException(SimpleATM.NOT_ENOUGH_MONEY);

        noteCount -= count;

        return new HashMap.SimpleEntry<>(noteType, count);
    }

    public Integer put(Integer count) {
        if (count <= 0)
            throw new RuntimeException(SimpleATM.INCORRECT_SUM);

        noteCount += count;

        return getBalance();
    }
}