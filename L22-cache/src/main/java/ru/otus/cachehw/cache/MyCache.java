package ru.otus.cachehw.cache;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {
    //Надо реализовать эти методы
    private static final Logger log = LoggerFactory.getLogger(MyCache.class);

    private final Map<K, V> storage = new WeakHashMap<>();
    private final List<HwListener<K, V>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        // удаляем, если есть и кладем новое value - для обновления кэша
        storage.remove(key);
        storage.put(key, value);
        notifyListeners(key, value, "PUT");
    }

    @Override
    public void remove(K key) {
        V value = storage.get(key);
        storage.remove(key);
        notifyListeners(key, value, "REMOVE");
    }

    public void removeAll() {
        storage.clear();
        notifyListeners(null, null, "REMOVE ALL");
    }

    @Override
    public V get(K key) {
        V value = storage.get(key);
        notifyListeners(key, value, "GET");
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

    private void notifyListeners(K key, V value, String action) {
        listeners.forEach(listener -> {
            try {
                listener.notify(key, value, action);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        });
    }
}
