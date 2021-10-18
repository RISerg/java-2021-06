package ru.otus.solid;

import java.util.*;
import java.util.stream.Collectors;

public class ATM {
    public static final String INCORRECT_SUM = "Некорректная сумма";
    public static final String NOT_ENOUGH_MONEY = "Недостаточно средств";
    public static final String MULTIPLICITY_ERROR = "Введите сумму, кратную %s";
    public static final String UNKNOWN_NOTE_TYPE = "Банкомат не поддерживает номинал: %s";

    private final int[] defaultNoteTypes = {5000, 2000, 1000, 500, 200, 100, 50, 10};

    private final Set<NoteType> noteTypes;
    private final NoteType minNote;
    private final Map<NoteType, Cell> cells;
    private int balance;

    public ATM(Set<NoteType> noteTypes) {
        // инициализируем типы банкнот, с которыми работает банкомат
        this.noteTypes = Optional.ofNullable(noteTypes).orElseGet(() -> Arrays.stream(defaultNoteTypes).mapToObj(NoteType::new).collect(Collectors.toSet()));
        this.minNote = this.noteTypes.stream().min(Comparator.comparingInt(NoteType::getValue)).orElseThrow();
        // инициализируем ячейки
        this.cells = new HashMap<NoteType, Cell>();
        this.noteTypes.forEach(noteType -> this.cells.put(noteType, new Cell(this, noteType)));
        // инициализируем баланс
        this.balance = 0;
    }

    public int getBalance() {
        return balance;
    }

    protected void updateBalance(int sum) {
        balance += sum;
    }

    private Cell getCell(NoteType noteType) {
        return cells.get(noteType);
    }

    private Cell getCell(int noteValue) {
        return cells.get(getNoteType(noteValue));
    }

    public NoteType getNoteType(int noteValue) {
        return this.noteTypes.stream()
                .filter(noteType -> noteType.getValue() == noteValue)
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format(UNKNOWN_NOTE_TYPE, noteValue)));
    }

    private List<Cell> getCellsBySum(int sum) {
        return this.noteTypes.stream()
                .filter(noteType -> noteType.getValue() <= sum)
                .filter(noteType -> cells.get(noteType).getBalance() >= sum)
                .map(cells::get)
                .collect(Collectors.toList());
    }

    private void checkSum(int sum) {
        if (sum <= 0)
            throw new RuntimeException(INCORRECT_SUM);
        if (sum > getBalance())
            throw new RuntimeException(NOT_ENOUGH_MONEY);
        if (noteTypes.stream().noneMatch(noteType -> sum % noteType.getValue() == 0))
            throw new RuntimeException(String.format(MULTIPLICITY_ERROR, minNote.getValue()));
    }

    public Map<NoteType, Integer> take(int sum) {
        checkSum(sum);

        var result = new HashMap<NoteType, Integer>();
        int total = 0;
        while (total < sum) {
            var cells = new TreeMap<NoteType, Cell>(this.cells);
            while (!cells.isEmpty()) {
                var noteType = cells.lastKey();
                var noteValue = noteType.getValue();
                if (sum - total >= noteValue) {
                    var count = (sum - total) / noteValue;
                    var cell = getCell(noteType);
                    if (cell.hasSomeNotes(count)) {
                        var notes = cell.take(count);
                        result.putAll(notes);
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

            if (noteTypes.stream().noneMatch(value -> value == noteType)) {
                unAcceptedNotes.put(noteType, count);
            } else {
                getCell(noteType).put(count);
            }
        }
        return unAcceptedNotes;
    }
}