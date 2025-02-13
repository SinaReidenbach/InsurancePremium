package com.sina_reidenbach.InsurancePremium.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Vehicle {

    @Id
    private Long Id;
    private String name;  // Name des Fahrzeugs (z.B. "Kleinwagen", "SUV", "Limousine")
    private double factor;  // Faktor für den Fahrzeugtyp

    public Vehicle(String name, double factor){
        this.name = name;
        this.factor = factor;
    }

    public Vehicle(){}

    // Getter und Setter

    public double getFactor() {
        return factor;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFactor(double factor) {
        this.factor = factor;
    }

    public String getName()  { return name; }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }
}
