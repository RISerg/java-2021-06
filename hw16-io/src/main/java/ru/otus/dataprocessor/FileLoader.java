package ru.otus.dataprocessor;

import com.google.gson.Gson;
import ru.otus.model.Measurement;

import javax.json.Json;
import java.util.Arrays;
import java.util.List;

public class FileLoader implements Loader {
    private final String fileName;

    public FileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        //читает файл, парсит и возвращает результат
        try (var reader = Json.createReader(FileLoader.class.getClassLoader().getResourceAsStream(this.fileName))) {
            var json = reader.read().toString();
            return Arrays.asList(new Gson().fromJson(json, Measurement[].class));
        }
    }
}
