package ru.otus.model.client;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "client")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "phone_id")
    private Phone phone;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "client_id", nullable = false, updatable = false)
    private List<Address> addressList;

    public Client(String name, Phone phone, List<Address>  addressList) {
        this.id = null;
        this.name = name;
        this.phone = phone;
        this.addressList = addressList;
    }

    @Override
    public Client clone() {
        return new Client(this.id, this.name, this.phone, this.addressList);
    }
}
