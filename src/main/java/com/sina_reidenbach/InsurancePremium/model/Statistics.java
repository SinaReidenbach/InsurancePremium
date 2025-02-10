package com.sina_reidenbach.InsurancePremium.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Statistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;            // Primärschlüssel
    private LocalDateTime dateTime;  // LocalDateTime für das Datum
    private String postcode;    // Postleitzahl
    private String vehicle;     // Fahrzeugname
    private int annokilometers; // Jährliche Kilometer
    private double premium;

    // Standard-Konstruktor
    public Statistics() {
    }

    public Statistics(LocalDateTime dateTime, String postcode, String vehicle, int annokilometers, double premium) {
        this.dateTime = dateTime;
        this.postcode = postcode;
        this.vehicle = vehicle;
        this.annokilometers = annokilometers;
        this.premium = premium;
    }

    // Getter und Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public int getAnnokilometers() {
        return annokilometers;
    }

    public void setAnnokilometers(int annokilometers) {
        this.annokilometers = annokilometers;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public double getPremium() {
        return premium;
    }

    public void setPremium(double premium) {
        this.premium = premium;
    }
}
