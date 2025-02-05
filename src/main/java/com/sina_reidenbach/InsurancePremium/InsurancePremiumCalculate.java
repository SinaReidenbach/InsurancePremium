package com.sina_reidenbach.InsurancePremium;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class InsurancePremiumCalculate {
    private final InsurancePremiumService insurancePremiumService;
    int km = 10000;
    String regionName= "Berlin";
    String vehicleName = "SUV";
    int basis = 500;

    public InsurancePremiumCalculate(InsurancePremiumService insurancePremiumService, JdbcTemplate jdbcTemplate) {

        this.insurancePremiumService = insurancePremiumService;
    }

    public void test() {

        double f1= insurancePremiumService.getKilometerFactor(km);
        double f2= insurancePremiumService.getRegionFactor(regionName);
        double f3= insurancePremiumService.getVehicleFactor(vehicleName);

        System.out.println("F1:" + f1 + " / F2:" + f2 + " / F3:" + f3);
        System.out.println("Gesamtfaktor: " + f1 * f2 * f3);
        System.out.println("Berechnete Prämie auf Basiswert von " + basis + "€: " + f1 * f2 * f3 * basis);



    }
}