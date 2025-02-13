package com.sina_reidenbach.InsurancePremium.model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private double factor;
    private String postcodeValue;

    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<City> cities;


    //Konstruktor
    public Region() {}

    // Getter und Setter
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

    public double getFactor() {
        return factor;
    }

    public void setFactor(double factor) {
        this.factor = factor;
    }

    public String getPostcodeValue() {
        return postcodeValue;
    }

    public void setPostcodeValue(String postcodeValue) {
        this.postcodeValue = postcodeValue;
    }

    public Set<City> getCities() {
        return cities;
    }

    public void setCities(Set<City> cities) {
        this.cities = cities;
    }
}
