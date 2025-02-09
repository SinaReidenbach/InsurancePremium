package com.sina_reidenbach.InsurancePremium.service;

import com.sina_reidenbach.InsurancePremium.model.AnnoKilometers;
import com.sina_reidenbach.InsurancePremium.model.Region;
import com.sina_reidenbach.InsurancePremium.model.Vehicle;
import com.sina_reidenbach.InsurancePremium.repository.AnnoKilometersRepository;
import com.sina_reidenbach.InsurancePremium.repository.RegionRepository;
import com.sina_reidenbach.InsurancePremium.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FactorService {

    @Autowired
    private final AnnoKilometersRepository annoKilometersRepository;

    @Autowired
    private final VehicleRepository vehicleRepository;
    @Autowired
    private final PostcodeService postcodeService;

    @Autowired
    private final RegionRepository regionRepository;

    public FactorService(AnnoKilometersRepository annoKilometersRepository, VehicleRepository vehicleRepository, PostcodeService postcodeService, RegionRepository regionRepository) {
        this.annoKilometersRepository = annoKilometersRepository;
        this.vehicleRepository = vehicleRepository;
        this.postcodeService = postcodeService;
        this.regionRepository = regionRepository;
    }

    // Diese Methode gibt den Kilometer-Faktor basierend auf der jährlichen Kilometerzahl zurück
    public double getKilometerFactor(int kilometers) {
        // Suche nach den passenden km-Bereichen, die die Kilometeranzahl abdecken
        List<AnnoKilometers> ranges = annoKilometersRepository.findByKmMinLessThanEqualAndKmMaxGreaterThanEqual(kilometers, kilometers);

        // Wenn eine passende Zeile gefunden wurde, gib den Factor zurück
        if (!ranges.isEmpty()) {
            return ranges.get(0).getKmFactor();  // Hier holen wir den richtigen Faktor aus der Tabelle
        }

        // Falls kein passender Bereich gefunden wurde, gib einen Standardwert zurück
        return 1.0;
    }

    // Diese Methode gibt den Fahrzeug-Faktor basierend auf dem Fahrzeugtyp zurück
    public double getVehicleFactor(String vehicleName) {
        // Holen des Fahrzeugtyps aus der DB
        Vehicle vehicle = vehicleRepository.findByVehicleName(vehicleName);
        if (vehicle != null) {
            return vehicle.getVehicleFactor();
        }
        return 1.0;  // Standardfaktor für unbekannte Fahrzeugtypen
    }

    public double getRegionFactorByPostcode(String postcode) {
        String regionName = postcodeService.getRegionByPostcode(postcode);
        return getRegionFactor(regionName);
    }

    // Diese Methode gibt den Region-Faktor basierend auf dem Regionennamen zurück
    public double getRegionFactor(String regionName) {
        if (regionName == null || regionName.equals("Region nicht gefunden")) {
            return 1.0;  // Standardfaktor, wenn die Region nicht existiert
        }

        Region region = regionRepository.findByRegionName(regionName);
        return (region != null) ? region.getRegionFactor() : 1.0;
    }

    // Berechnung der Versicherungsprämie unter Berücksichtigung der Eingaben
    public double calculatePremium(int kilometers, String vehicleName, String regionName) {
        double kmFactor = getKilometerFactor(kilometers);  // Kilometer-Faktor aus der DB
        double vehicleFactor = getVehicleFactor(vehicleName);  // Fahrzeug-Faktor aus der DB
        double regionFactor = getRegionFactor(regionName);  // Region-Faktor aus der DB

        return kmFactor * vehicleFactor * regionFactor;
    }
}