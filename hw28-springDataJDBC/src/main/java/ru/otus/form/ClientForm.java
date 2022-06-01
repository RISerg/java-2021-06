package ru.otus.form;

public class ClientForm {

    private String name;
    private String phone;
    private String address;

    public ClientForm(String name, String phone, String address) {
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    public ClientForm() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
