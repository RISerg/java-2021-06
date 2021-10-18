package ru.otus.solid;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

class ATMTest {
    private ATM atm;
    private static int[] noteValues;
    private static int noteCount;
    private static int expectedBalance;
    private static Set<NoteType> noteTypes;

    @BeforeAll
    static void ATMTestSetUp() {
        noteValues = new int[]{5_000, 2_000, 1_000, 500, 200, 100};
        noteCount = 10;
        expectedBalance = Arrays.stream(noteValues).map(value -> value * noteCount).sum();
    }

    @BeforeEach
    void setUp() {
        atm = new ATM(noteTypes);
        noteTypes = Arrays.stream(noteValues).mapToObj(value -> atm.getNoteType(value)).collect(Collectors.toSet());
        var money = new HashMap<NoteType, Integer>();
        noteTypes.forEach(noteType -> money.put(noteType, noteCount));
        atm.put(money);
    }

    @Test
    void getBalance() {
        Assertions.assertEquals(expectedBalance, atm.getBalance());
    }

    @Test
    void takeMoreThanHas() {
        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> atm.take(expectedBalance + 1));
        Assertions.assertEquals(ATM.NOT_ENOUGH_MONEY, exception.getMessage());
    }

    @Test
    void takeIncorrectSum() {
        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> atm.take(-1));
        Assertions.assertEquals(ATM.INCORRECT_SUM, exception.getMessage());
    }

    @Test
    void multiplicityCheck() {
        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> atm.take(1));
        String expectedMessage = ATM.MULTIPLICITY_ERROR;
        Assertions.assertTrue(exception.getMessage().startsWith(expectedMessage.substring(0, expectedMessage.indexOf(' '))));
    }

    @Test
    void take() {
        var expectedNotes = new HashMap<NoteType, Integer>();
        expectedNotes.put(atm.getNoteType(2_000), 1);
        expectedNotes.put(atm.getNoteType(1_000), 1);
        expectedNotes.put(atm.getNoteType(200), 1);
        var expectedNotesSum = expectedNotes.keySet().stream().mapToInt(NoteType::getValue).sum();
        atm.put(expectedNotes);
        var takenNotes = atm.take(expectedNotesSum);

        Assertions.assertEquals(expectedNotes, takenNotes);
    }

    @Test
    void put() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        Method getCellMethod = ATM.class.getDeclaredMethod("getCell", int.class);
        getCellMethod.setAccessible(true);

        Field noteCountField = Cell.class.getDeclaredField("noteCount");
        noteCountField.setAccessible(true);

        for (int value : noteValues) {
            Cell cell = (Cell) getCellMethod.invoke(atm, value);
            Assertions.assertEquals(noteCount, noteCountField.getInt(cell));
        }
    }

    @Test
    void putUnsupportedNote() {
        var notes = new HashMap<NoteType, Integer>();
        notes.put(new NoteType(10_000), 1);
        var returnedNotes = atm.put(notes);

        Assertions.assertEquals(notes, returnedNotes);
    }
}