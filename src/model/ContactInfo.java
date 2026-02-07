package model;

import java.io.Serializable;

public class ContactInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private  String name;
    private String lastName;
    private String address;

    public ContactInfo(String name, String lastName, String address) {
        this.name = name;
        this.lastName = lastName;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return name + " " + lastName + " | " + address;
    }
}
