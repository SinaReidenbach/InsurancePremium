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
    private int kmMin;      // Mindest-Kilometerstand
    private int kmMax;      // Maximal-Kilometerstand
    private double kmFactor;   // Faktor

    public AnnoKilometers(int kmMin, int kmMax, double kmFactor) {
        this.kmMin = kmMin;
        this.kmMax = kmMax;
        this.kmFactor = kmFactor;
    }

    public AnnoKilometers() {}

    // Getter und Setter
    public double getKmFactor() {
        return kmFactor;
    }
    public int getKmMin() {
        return kmMin;
    }

    public void setKmMin(int i) {
    }

    public int getKmMax() {
        return kmMax;
    }

    public void setKmMax(int i) {
    }

    public void setKmFactor(double v) {
    }
}