package ru.otus.dataprocessor;

import ru.otus.model.Measurement;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProcessorAggregator implements Processor {

    @Override
    public Map<String, Double> process(List<Measurement> data) {
        //группирует выходящий список по name, при этом суммирует поля value
        return data.stream().collect(
                Collectors.groupingBy(
                        Measurement::getName,
                        TreeMap::new,
                        Collectors.mapping(
                                Measurement::getValue,
                                Collectors.summingDouble(Double::intValue))));
    }
}
