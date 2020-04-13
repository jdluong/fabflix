package com.fabflix.fabflix;

public class Customer {
    private int id;
    private String firstName;
    private String lastName;
    private String ccId;
    private String address;
    private String email;
    private String password;

    public Customer(int id, String firstName, String lastName, String ccId, String address, String email, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.ccId = ccId;
        this.address = address;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return this.id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastNamString() {
        return this.lastName;
    }

    public String getAddress() {
        return this.address;
    }

    public String getEmail() {
        return this.email;
    }

    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName=" + lastName +
                ", address='" + address + '\'' +
                ", ccId (last 4 digits)='" + ccId.substring(15, 20) + '\'' +
                ", email='" + email + '\'' +
                ", password (length)='" + password.length() + '\'' +
                '}';
    }
}

