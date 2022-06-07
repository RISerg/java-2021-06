package ru.otus.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Set;

@Table("client")
public class Client /*implements Cloneable*/ {

    @Id
    private Long id;
    private String name;
    @MappedCollection(idColumn = "id")
    private Phone phone;
    @MappedCollection(idColumn = "client_id")
    private Set<Address> addresses;

    @PersistenceConstructor
    public Client(Long id, String name, Phone phone, Set<Address> addresses) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.addresses = addresses;
    }

    public Client() {
    }

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

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone=" + phone +
                ", addresses=" + addresses +
                '}';
    }
//    @Override
//    public Client clone() {
//        return new Client(this.id, this.name, this.phone, this.addresses);
//    }
}
