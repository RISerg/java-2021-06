package ru.otus.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Table("client")
public class Client implements Cloneable {

    @Id
    private Long id;
    private String name;
    private Phone phone;
    @MappedCollection(idColumn = "client_id")
    private List<Address> addressList;

    public Client() {

    }

    @PersistenceConstructor
    public Client(Long id, String name, Phone phone, List<Address> addressList) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.addressList = addressList;
    }

    @Override
    public Client clone() {
        return new Client(this.id, this.name, this.phone, this.addressList);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public List<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }
}
