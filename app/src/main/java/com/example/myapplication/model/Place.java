package com.example.myapplication.model;

import java.math.BigDecimal;
import java.util.List;

public class Place {
    private Long id;
    private String name;
    private Address address;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private Type type;
    private List<Opinion> opinions;
    private boolean is_accepted;

    public Place() {
    }

    public Place(Long id, String name, Address address, BigDecimal longitude, BigDecimal latitude, Type type, List<Opinion> opinions, boolean is_accepted) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.type = type;
        this.opinions = opinions;
        this.is_accepted = is_accepted;
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<Opinion> getOpinions() {
        return opinions;
    }

    public void setOpinions(List<Opinion> opinions) {
        this.opinions = opinions;
    }

    public boolean isIs_accepted() {
        return is_accepted;
    }

    public void setIs_accepted(boolean is_accepted) {
        this.is_accepted = is_accepted;
    }

    @Override
    public String toString() {
        return "Place{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address=" + address +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", type=" + type +
                ", opinions=" + opinions +
                ", is_accepted=" + is_accepted +
                '}';
    }
}
