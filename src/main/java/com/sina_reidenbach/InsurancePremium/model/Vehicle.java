package com.sina_reidenbach.InsurancePremium.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Vehicle {

    @Id
    private int vehicleId;
    private String vehicleName;  // Name des Fahrzeugs (z.B. "Kleinwagen", "SUV", "Limousine")
    private double vehicleFactor;  // Faktor für den Fahrzeugtyp

    // Getter und Setter

    public double getVehicleFactor() {
        return vehicleFactor;
    }
}