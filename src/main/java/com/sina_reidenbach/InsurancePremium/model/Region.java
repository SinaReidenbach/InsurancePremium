package com.sina_reidenbach.InsurancePremium.model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "region", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private double factor;

    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<City> cities;


    // Constructor
    public Region(String name, double factor) {
        this.name = name;
        this.factor = factor;
    }

    public Region() {}

    public Region(long id, String name, double factor) {
        this.id = id;
        this.name = name;
        this.factor = factor;
    }

    // Getter und Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getFactor() {
        return factor;
    }

    public void setFactor(double factor) {
        this.factor = factor;
    }

    public Set<City> getCities() {
        return cities;
    }

    public void setCities(Set<City> cities) {
        this.cities = cities;
    }
}
