package ru.otus.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Table;

@Table("address")
public class Address /*implements Cloneable*/ {

    @Id
    private Long id;
    private String street;

    @PersistenceConstructor
    public Address(Long id, String street) {
        this.id = id;
        this.street = street;
    }

    public Address(String street) {
        this.id = null;
        this.street = street;
    }

    public Address() {
    }

    public Long getId() {
        return this.id;
    }

    public String getStreet() {
        return this.street;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String toString() {
        return this.street;
    }

//    @Override
//    public Address clone() {
//        return new Address(this.id, this.street);
//    }
}
