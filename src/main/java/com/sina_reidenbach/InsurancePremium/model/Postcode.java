package com.sina_reidenbach.InsurancePremium.model;

import jakarta.persistence.*;

@Entity
public class Postcode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String postcodeValue;

    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private City city;
    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

    // Getter und Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPostcodeValue() { return postcodeValue; }
    public void setPostcodeValue(String postcodeValue) { this.postcodeValue = postcodeValue; }
    public City getCity() { return city; }
    public void setCity(City city) { this.city = city; }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }
}