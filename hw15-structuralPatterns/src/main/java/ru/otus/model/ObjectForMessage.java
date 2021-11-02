package ru.otus.model;

import java.util.ArrayList;
import java.util.List;

public class ObjectForMessage {
    private List<String> data;

    public ObjectForMessage() {}

    public ObjectForMessage(List<String> data) {
        this.data = data;
    }

    public ObjectForMessage(ObjectForMessage object) {
        this.data = new ArrayList<>(object.getData());
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
