package com.sina_reidenbach.InsurancePremium.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Vehicle {

    @Id
    private int vehicleId;
    private String vehicleName;  // Name des Fahrzeugs (z.B. "Kleinwagen", "SUV", "Limousine")
    private double vehicleFactor;  // Faktor für den Fahrzeugtyp

    public Vehicle(String vehicleName,double vehicleFactor){
        this.vehicleName = vehicleName;
        this.vehicleFactor = vehicleFactor;
    }

    public Vehicle(){}

    // Getter und Setter

    public double getVehicleFactor() {
        return vehicleFactor;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public void setVehicleFactor(double vehicleFactor) {
        this.vehicleFactor = vehicleFactor;
    }

    public String getVehicleName()  { return vehicleName; }
}
