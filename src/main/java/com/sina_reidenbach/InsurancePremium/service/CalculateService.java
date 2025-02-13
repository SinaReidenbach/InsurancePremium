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
    double premium=0;

    public CalculateService(FactorService insurancePremiumService, JdbcTemplate jdbcTemplate, PostcodeService postcodeService) {

        this.factorService = insurancePremiumService;
        this.postcodeService = postcodeService;
    }

    public double calculatePremium(int km, String postcode, String vehicleName) {

        String regionName = postcodeService.getRegionByPostcode(postcode);

        double f1 = factorService.getKilometerFactor(km);
        double f2 = factorService.getRegionFactorByPostcode(postcode);
        double f3 = factorService.getVehicleFactor(vehicleName);

        System.err.println("Alle Faktoren gespeichert.");
        System.err.println("F1: " + f1 + " / F2: " + f2 + " / F3: " + f3);
        System.err.println("Gesamtfaktor: " + f1*f2*f3);
        premium = f1 * f2 * f3 * basis;
        System.err.println("Berechnete Prämie: " + premium);

        return premium;
    }
}
        //Speichern der eingaben in der datenbank anonymisiert
        //HTML-API für Drittanbieter
        //Frontend zur Eingabe der parameter

        /*„Ich stimme der Speicherung meiner IP-Adresse zu,
        um die Berechnungen zu ermöglichen und die Nutzung zu
        statistischen Zwecken zu unterstützen.
        Weitere Informationen finden Sie in der Datenschutzerklärung.“
         */
