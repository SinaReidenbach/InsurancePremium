package com.sina_reidenbach.InsurancePremium;

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
public class InsurancePremiumService {

    @Autowired
    private AnnoKilometersRepository annoKilometersRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private RegionRepository regionRepository;

    // Diese Methode gibt den Kilometer-Faktor basierend auf der jährlichen Kilometerzahl zurück
    public double getKilometerFactor(int kilometers) {
        // Suche nach den passenden km-Bereichen
        List<AnnoKilometers> ranges = annoKilometersRepository.findByKmMinLessThanEqualAndKmMaxGreaterThanEqual(kilometers);

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

    // Diese Methode gibt den Region-Faktor basierend auf dem Regionennamen zurück
    public double getRegionFactor(String regionName) {
        // Holen der Region aus der DB
        Region region = regionRepository.findByRegionName(regionName);
        if (region != null) {
            return region.getRegionFactor();
        }
        return 1.0;  // Standardfaktor für unbekannte Regionen
    }

    // Berechnung der Versicherungsprämie unter Berücksichtigung der Eingaben
    public double calculatePremium(int kilometers, String vehicleName, String regionName) {
        double kmFactor = getKilometerFactor(kilometers);  // Kilometer-Faktor aus der DB
        double vehicleFactor = getVehicleFactor(vehicleName);  // Fahrzeug-Faktor aus der DB
        double regionFactor = getRegionFactor(regionName);  // Region-Faktor aus der DB

        return kmFactor * vehicleFactor * regionFactor;
    }
}