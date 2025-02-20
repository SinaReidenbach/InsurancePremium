package com.sina_reidenbach.InsurancePremium.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;  // Name des Fahrzeugs (z.B. "Kleinwagen", "SUV", "Limousine")
    private double factor;  // Faktor für den Fahrzeugtyp

    public Vehicle(String name, double factor){
        this.name = name;
        this.factor = factor;
    }

    public Vehicle(){}

    public Vehicle(long id, String name, double factor) {
        this.id = id;
        this.name = name;
        this.factor = factor;
    }


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
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
