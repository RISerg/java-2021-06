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

class SimpleATMTest {
    private SimpleATM atm;
    private static NoteType[] noteTypes;
    private static int noteCount;
    private static int expectedBalance;

    @BeforeAll
    static void ATMTestSetUp() {
        noteTypes = new NoteType[]{NoteType.P5000, NoteType.P2000, NoteType.P1000, NoteType.P500, NoteType.P200, NoteType.P100};
        noteCount = 10;
        expectedBalance = Arrays.stream(noteTypes).mapToInt(NoteType::getValue).sum() * noteCount;
    }

    @BeforeEach
    void setUp() {
        this.atm = new SimpleATM(noteTypes);
        var money = new HashMap<NoteType, Integer>();
        Arrays.stream(noteTypes).forEach(noteType -> money.put(noteType, noteCount));
        this.atm.put(money);
    }

    @Test
    void getBalance() {
        Assertions.assertEquals(expectedBalance, atm.getBalance());
    }

    @Test
    void takeMoreThanHas() {
        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> atm.take(expectedBalance + 1));
        Assertions.assertEquals(SimpleATM.NOT_ENOUGH_MONEY, exception.getMessage());
    }

    @Test
    void takeIncorrectSum() {
        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> atm.take(-1));
        Assertions.assertEquals(SimpleATM.INCORRECT_SUM, exception.getMessage());
    }

    @Test
    void multiplicityCheck() {
        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> atm.take(1));
        String expectedMessage = SimpleATM.MULTIPLICITY_ERROR;
        Assertions.assertTrue(exception.getMessage().startsWith(expectedMessage.substring(0, expectedMessage.indexOf(' '))));
    }

    @Test
    void take() {
        var expectedNotes = new HashMap<NoteType, Integer>();
        expectedNotes.put(NoteType.P2000, 1);
        expectedNotes.put(NoteType.P1000, 1);
        expectedNotes.put(NoteType.P200, 1);
        var expectedNotesSum = expectedNotes.keySet().stream().mapToInt(NoteType::getValue).sum();
        atm.put(expectedNotes);
        var takenNotes = atm.take(expectedNotesSum);

        Assertions.assertEquals(expectedNotes, takenNotes);
    }

    @Test
    void put() throws NoSuchMethodException, IllegalAccessException, NoSuchFieldException, InvocationTargetException {
        Method getCellMethod = SimpleATM.class.getDeclaredMethod("getCell", NoteType.class);
        getCellMethod.setAccessible(true);

        Field noteCountField = SimpleCell.class.getDeclaredField("noteCount");
        noteCountField.setAccessible(true);

        for (NoteType noteType : noteTypes) {
            SimpleCell cell = (SimpleCell) getCellMethod.invoke(atm, noteType);
            Assertions.assertEquals(noteCount, noteCountField.get(cell));
        }
    }

    @Test
    void putUnsupportedNote() {
        var notes = new HashMap<NoteType, Integer>();
        notes.put(NoteType.P50, 1);
        var returnedNotes = atm.put(notes);

        Assertions.assertEquals(notes, returnedNotes);
    }
}