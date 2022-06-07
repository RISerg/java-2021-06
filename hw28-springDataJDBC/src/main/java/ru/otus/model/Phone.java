package ru.otus.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Table;

@Table("phone")
public class Phone /*implements Cloneable*/ {

    @Id
    private Long id;
    private String number;

    @PersistenceConstructor
    public Phone(Long id, String number) {
        this.id = id;
        this.number = number;
    }

    public Phone(String number) {
        this.number = number;
    }

    public Phone() {
    }

    public Long getId() {
        return this.id;
    }

    public String getNumber() {
        return this.number;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String toString() {
        return this.number;
    }

//    @Override
//    public Phone clone() {
//        return new Phone(this.id, this.number);
//    }
}
