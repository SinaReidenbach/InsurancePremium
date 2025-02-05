package com.sina_reidenbach.InsurancePremium.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
@Entity
public class Region {

    @Id
    private String regionName;  // Name der Region (z.B. "Berlin", "Hamburg", "München")
    private double regionFactor;  // Faktor für die Region

    // Getter und Setter
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
}