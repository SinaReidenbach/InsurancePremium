package com.sina_reidenbach.InsurancePremium.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class AnnoKilometers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int kmId;       // Primärschlüssel
    private double kmFactor;   // Faktor
    private int kmMin;      // Mindest-Kilometerstand
    private int kmMax;      // Maximal-Kilometerstand

    // Getter und Setter
    public int getKmId() {
        return kmId;
    }

    public void setKmId(int km_Id) {
        this.kmId = km_Id;
    }

    public double getKmFactor() {
        return kmFactor;
    }

    public void setKmFactor(double kmFactor) {
        this.kmFactor = kmFactor;
    }

    public int getKmMin() {
        return kmMin;
    }

    public void setKmMin(int kmMin) {
        this.kmMin = kmMin;
    }

    public int getKmMax() {
        return kmMax;
    }

    public void setKmMax(int kmMax) {
        this.kmMax = kmMax;
    }
}