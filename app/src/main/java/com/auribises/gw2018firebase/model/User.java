package com.auribises.gw2018firebase.model;

public class User {

    public String name;
    public String phone;
    public String email;
    public Integer age;
    public String password;
    public String address;


    public User(){

    }

    public User(String name, String email, String password, String address) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
