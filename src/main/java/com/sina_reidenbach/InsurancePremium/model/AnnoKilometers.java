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

    // Getter und Setter
    public double getKmFactor() {
        return kmFactor;
    }

}