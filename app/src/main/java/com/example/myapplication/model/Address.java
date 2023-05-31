package com.example.myapplication.model;

public class Address {
    private Long id;
    private String street;
    private int number;
    private String city;

    public Address() {
    }

    public Address(Long id, String street, int number, String city) {
        this.id = id;
        this.street = street;
        this.number = number;
        this.city = city;
    }

    public Address(String street, int number, String city) {
        this.street = street;
        this.number = number;
        this.city = city;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", street='" + street + '\'' +
                ", number=" + number +
                ", city='" + city + '\'' +
                '}';
    }
}
