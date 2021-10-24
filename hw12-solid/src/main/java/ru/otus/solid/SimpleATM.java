package ru.otus.solid;

import java.util.*;

public class SimpleATM implements ATM {
    public static final String INCORRECT_SUM = "Некорректная сумма";
    public static final String NOT_ENOUGH_MONEY = "Недостаточно средств";
    public static final String MULTIPLICITY_ERROR = "Введите сумму, кратную %s";
    public static final String UNKNOWN_NOTE_TYPE = "Банкомат не поддерживает номинал: %s";

    private final NoteType[] noteTypes;
    private final NoteType minNote;
    private final Map<NoteType, Cell> cells;

    public SimpleATM(NoteType[] noteTypes) {
        // инициализируем типы банкнот, с которыми работает банкомат
        this.noteTypes = Optional.ofNullable(noteTypes).orElseGet(NoteType::values);
        this.minNote = Arrays.stream(this.noteTypes).min(Comparator.comparingInt(NoteType::getValue)).orElseThrow();
        // инициализируем ячейки
        this.cells = new HashMap<>();
        Arrays.stream(this.noteTypes).forEach(noteType -> this.cells.put(noteType, new SimpleCell(noteType)));
    }

    public Integer getBalance() {
        return this.cells.values().stream().mapToInt(Cell::getBalance).sum();
    }

    public Map<NoteType, Integer> take(Integer sum) {
        checkSum(sum);

        var result = new HashMap<NoteType, Integer>();
        int total = 0;
        while (total < sum) {
            var cells = new TreeMap<NoteType, Cell>(this.cells);
            while (!cells.isEmpty()) {
                var noteType = cells.firstKey();
                var noteValue = noteType.getValue();
                if (sum - total >= noteValue) {
                    var count = (sum - total) / noteValue;
                    var cell = this.getCell(noteType);
                    if (cell.hasSomeNotes(count)) {
                        var notes = cell.take(count);
                        result.put(notes.getKey(), notes.getValue());
                        total += noteValue * count;
                    }
                }
                cells.remove(noteType);
            }
        }

        return result;
    }

    public Map<NoteType, Integer> put(Map<NoteType, Integer> notes) {
        Map<NoteType, Integer> unAcceptedNotes = new HashMap<>();
        for (var entity : notes.entrySet()) {
            var noteType = entity.getKey();
            var count = entity.getValue();

            if (Arrays.stream(this.noteTypes).noneMatch(value -> value == noteType)) {
                unAcceptedNotes.put(noteType, count);
            } else {
                getCell(noteType).put(count);
            }
        }
        return unAcceptedNotes;
    }

    private void checkSum(Integer sum) {
        if (sum <= 0)
            throw new RuntimeException(INCORRECT_SUM);
        if (sum > getBalance())
            throw new RuntimeException(NOT_ENOUGH_MONEY);
        if (Arrays.stream(this.noteTypes).noneMatch(noteType -> sum % noteType.getValue() == 0))
            throw new RuntimeException(String.format(MULTIPLICITY_ERROR, minNote.getValue()));
    }

    private Cell getCell(NoteType noteType) {
        return this.cells.get(noteType);
    }
}