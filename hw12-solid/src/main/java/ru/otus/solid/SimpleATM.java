package ru.otus.solid;

import java.util.*;
import java.util.stream.Collectors;

public class SimpleATM implements ATM {
    public static final String INCORRECT_SUM = "Некорректная сумма";
    public static final String NOT_ENOUGH_MONEY = "Недостаточно средств";
    public static final String MULTIPLICITY_ERROR = "Введите сумму, кратную %s";
    public static final String UNKNOWN_NOTE_TYPE = "Банкомат не поддерживает номинал: %s";

    private final int[] defaultNoteTypes = {5000, 2000, 1000, 500, 200, 100, 50, 10};

    private final Set<NoteType> noteTypes;
    private final NoteType minNote;
    private final Map<NoteType, SimpleCell> cells;

    public SimpleATM(Set<NoteType> noteTypes) {
        // инициализируем типы банкнот, с которыми работает банкомат
        this.noteTypes = Optional.ofNullable(noteTypes).orElseGet(() -> Arrays.stream(defaultNoteTypes).mapToObj(NoteType::new).collect(Collectors.toSet()));
        this.minNote = this.noteTypes.stream().min(Comparator.comparingInt(NoteType::getValue)).orElseThrow();
        // инициализируем ячейки
        this.cells = new HashMap<NoteType, SimpleCell>();
        this.noteTypes.forEach(noteType -> this.cells.put(noteType, new SimpleCell(noteType)));
    }

    public Integer getBalance() {
        return this.cells.values().stream().mapToInt(SimpleCell::getBalance).sum();
    }

    public Map<NoteType, Integer> take(Integer sum) {
        checkSum(sum);

        var result = new HashMap<NoteType, Integer>();
        int total = 0;
        while (total < sum) {
            var cells = new TreeMap<NoteType, SimpleCell>(this.cells);
            while (!cells.isEmpty()) {
                var noteType = cells.lastKey();
                var noteValue = noteType.getValue();
                if (sum - total >= noteValue) {
                    var count = (sum - total) / noteValue;
                    var cell = getCell(noteType);
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

            if (noteTypes.stream().noneMatch(value -> value == noteType)) {
                unAcceptedNotes.put(noteType, count);
            } else {
                getCell(noteType).put(count);
            }
        }
        return unAcceptedNotes;
    }

    public NoteType getNoteType(Integer noteValue) {
        return this.noteTypes.stream()
                .filter(noteType -> noteType.getValue().equals(noteValue))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format(UNKNOWN_NOTE_TYPE, noteValue)));
    }

    private void checkSum(Integer sum) {
        if (sum <= 0)
            throw new RuntimeException(INCORRECT_SUM);
        if (sum > getBalance())
            throw new RuntimeException(NOT_ENOUGH_MONEY);
        if (noteTypes.stream().noneMatch(noteType -> sum % noteType.getValue() == 0))
            throw new RuntimeException(String.format(MULTIPLICITY_ERROR, minNote.getValue()));
    }

    private SimpleCell getCell(NoteType noteType) {
        return cells.get(noteType);
    }

    private List<SimpleCell> getCellsBySum(Integer sum) {
        return this.noteTypes.stream()
                .filter(noteType -> noteType.getValue() <= sum)
                .filter(noteType -> cells.get(noteType).getBalance() >= sum)
                .map(cells::get)
                .collect(Collectors.toList());
    }
}