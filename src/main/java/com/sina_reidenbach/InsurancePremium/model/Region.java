package com.sina_reidenbach.InsurancePremium.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Region {
    @Id
    private int regionId;
    private String postcode;
    private String regionName;
    private double regionFactor;

    // **Standard-Konstruktor erforderlich für JPA**
    public Region() {
    }

    // Dein bestehender Konstruktor
    public Region(String postcode, String regionName) {
        this.postcode = postcode;
        this.regionName = regionName;
    }

    // Getter und Setter
    public String getPostcode() {
        return postcode;
    }

    public String getRegion() {
        return regionName;
    }

    public double getRegionFactor() {
        return regionFactor;
    }
}