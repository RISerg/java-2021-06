package ru.otus.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Table;

@Table("phone")
public class Phone implements Cloneable {

    @Id
    private Long id;
    private String number;

    @PersistenceConstructor
    public Phone(Long id, String number) {
        this.id = id;
        this.number = number;
    }

    public Phone(String number) {
        this.id = null;
        this.number = number;
    }

    public Phone() {
    }

    @Override
    public Phone clone() {
        return new Phone(this.id, this.number);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String toString() {
        return this.number;
    }
}
