package com.sina_reidenbach.InsurancePremium.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Anno_Kilometers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int min;
    private int max;
    private double factor;

    // Constructor
    public Anno_Kilometers(int min, int max, double factor) {
        this.min = min;
        this.max = max;
        this.factor = factor;
    }

    public Anno_Kilometers() {}

    public Anno_Kilometers(long id, int min, int max, double factor) {
        this.id = id;
        this.min = min;
        this.max = max;
        this.factor = factor;
    }

    // Getter und Setter
    public double getFactor() {
        return factor;
    }
    public int getMin() {
        return min;
    }

    public void setMin(int i) {
    }

    public int getMax() {
        return max;
    }

    public void setMax(int i) {
    }

    public void setFactor(double v) {
    }

    public Long getId() {
        return id;
    }
}