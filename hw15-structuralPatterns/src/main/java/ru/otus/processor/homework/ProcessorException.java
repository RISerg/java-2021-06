package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

public class ProcessorException implements Processor {
    private final DateTimeProvider dateTimeProvider;

    public ProcessorException(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public Message process(Message message) {
        int second = dateTimeProvider.getDate().getSecond();
        if ((second & 1) == 0)
            throw new RuntimeException("Even second exception: " + second);
//        return message.toBuilder().field1(second + "").build();
        return message;
    }
}
