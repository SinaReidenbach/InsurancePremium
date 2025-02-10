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

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public double getRegionFactor() {
        return regionFactor;
    }

    public void setRegionFactor(double regionFactor) {
        this.regionFactor = regionFactor;
    }

    public int getRegionId() { return regionId; }
    public void setRegionId(int regionId) { this.regionId = regionId; }


}