package ru.otus.crm.service;

import java.util.List;
import java.util.Optional;

public interface DBService<T> {

    T save(T record);

    Optional<T> get(long id);

    List<T> findAll();
}
