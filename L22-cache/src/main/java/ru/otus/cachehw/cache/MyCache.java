package ru.otus.cachehw.cache;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {
//Надо реализовать эти методы

    private final Map<K, V> storage = new WeakHashMap<>();
    private final List<HwListener<K, V>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        // удаляем, если есть и кладем новое value - для обновления кэша
        storage.remove(key);
        storage.put(key, value);
        listeners.forEach(listener -> listener.notify(key, value, "PUT"));
    }

    @Override
    public void remove(K key) {
        V value = storage.get(key);
        storage.remove(key);
        listeners.forEach(listener -> listener.notify(key, value, "REMOVE"));
    }

    @Override
    public V get(K key) {
        V value = storage.get(key);
        listeners.forEach(listener -> listener.notify(key, value, "GET"));
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }
}
