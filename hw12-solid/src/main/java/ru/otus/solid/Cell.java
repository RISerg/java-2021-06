package ru.otus.solid;

import java.util.HashMap;
import java.util.Map;

public class Cell {

    private final ATM atm;
    private final NoteType noteType;
    private int noteCount;

    public Cell(ATM atm, NoteType noteType) {
        this.atm = atm;
        this.noteType = noteType;
        this.noteCount = 0;
    }

    public int getBalance() {
        return noteCount * noteType.getValue();
    }

    public boolean hasSomeNotes(int count) {
        return noteCount >= count;
    }

    public Map<NoteType, Integer> take(int count) {
        if (count > noteCount)
            throw new RuntimeException(ATM.NOT_ENOUGH_MONEY);

        noteCount -= count;
        atm.updateBalance(noteType.getValue() * (-count));

        var result = new HashMap<NoteType, Integer>();
        result.put(noteType, count);

        return result;
    }

    public int put(int count) {
        noteCount += count;
        atm.updateBalance(noteType.getValue() * count);
        return getBalance();
    }
}