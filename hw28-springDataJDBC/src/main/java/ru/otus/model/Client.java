package ru.otus.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Set;

@Table("client")
public class Client implements Persistable<Long> {

    @Id
    private Long id;
    private String name;
    @MappedCollection(idColumn = "id")
    private Phone phone;
    @MappedCollection(idColumn = "client_id")
    private Set<Address> addresses;

    @Transient
    private boolean isNew;

    public Client(Long id, String name, Phone phone, Set<Address> addresses, boolean isNew) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.addresses = addresses;
        this.isNew = isNew;
    }

    @PersistenceConstructor
    public Client(Long id, String name, Phone phone, Set<Address> addresses) {
        this(id, name, phone, addresses, false);
    }

    public Client() {
        this.isNew = true;
    }

    @Override
    public boolean isNew() {
        return this.isNew;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Phone getPhone() {
        return this.phone;
    }

    public Set<Address> getAddresses() {
        return this.addresses;
    }

    public boolean getIsNew() {
        return this.isNew;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }

    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone=" + phone +
                ", addresses=" + addresses +
                ", isNew=" + isNew +
                '}';
    }
}
