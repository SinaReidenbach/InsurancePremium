package com.sina_reidenbach.InsurancePremium.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class CalculateService {
    private final FactorService factorService;
    private final PostcodeService postcodeService;
    int km = 10000;
    String regionName;
    String postcode = "79189";
    String vehicleName = "SUV";
    int basis = 500;

    public CalculateService(FactorService insurancePremiumService, JdbcTemplate jdbcTemplate, PostcodeService postcodeService) {

        this.factorService = insurancePremiumService;
        this.postcodeService = postcodeService;
    }

    public void test() {

        regionName = postcodeService.getRegionByPostcode(postcode);

        double f1= factorService.getKilometerFactor(km);
        double f2= factorService.getRegionFactorByPostcode(postcode);
        double f3= factorService.getVehicleFactor(vehicleName);

        System.out.println("F1 ("+km+"km):" + f1 + " / F2 (" + postcode + " = " + regionName + "):" + f2 + " / F3 (" + vehicleName+"):" + f3);
        System.out.println("Gesamtfaktor: " + f1 * f2 * f3);
        System.out.println("Berechnete Prämie auf Basiswert von " + basis + "€: " + f1 * f2 * f3 * basis);

        //Eingabeoberfläche
        //Speichern der eingaben in der datenbank anonymisiert
        //HTML-API für Drittanbieter
        //Frontend zur Eingabe der parameter
    }
}