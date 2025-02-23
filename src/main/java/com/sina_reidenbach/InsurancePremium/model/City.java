package com.sina_reidenbach.InsurancePremium.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;

    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Postcode> postcodes;

    //Constructor
    public City() {}

    public City(String name) {
        this.name = name;
    }

    public City(String name, Region region) {
        this.name = name;
        this.region = region;
    }

    // Getter und Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Region getRegion() { return region; }
    public void setRegion(Region region) { this.region = region; }
    public Set<Postcode> getPostcodes() { return postcodes; }
    public void setPostcodes(Set<Postcode> postcodes) { this.postcodes = postcodes; }
}