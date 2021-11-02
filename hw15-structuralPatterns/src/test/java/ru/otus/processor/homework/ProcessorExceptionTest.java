package ru.otus.processor.homework;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ProcessorExceptionTest {

    @Test
    @DisplayName("Исключение выбрасывается в четную секунду")
    void processTest() {
        var message = new Message.Builder(1L).build();
        var time = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0)); // четная секунда
        var processor = new ProcessorException(() -> time);

        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> processor.process(message));
    }
}