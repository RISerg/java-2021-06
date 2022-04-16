package ru.otus.cachehw.cache;


public interface HwCache<K, V> {

    void put(K key, V value);

    void remove(K key);

    void removeAll();

    V get(K key);

    void addListener(HwListener<K, V> listener);

    void removeListener(HwListener<K, V> listener);
}
