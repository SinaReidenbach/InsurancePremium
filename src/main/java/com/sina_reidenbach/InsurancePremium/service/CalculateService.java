package com.sina_reidenbach.InsurancePremium.service;

import com.sina_reidenbach.InsurancePremium.model.Anno_Kilometers;
import com.sina_reidenbach.InsurancePremium.model.Region;
import com.sina_reidenbach.InsurancePremium.model.Vehicle;
import com.sina_reidenbach.InsurancePremium.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class CalculateService {

    private static final Logger logger = LoggerFactory.getLogger(CalculateService.class);

    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private AnnoKilometersRepository annoKilometersRepository;
    @Autowired
    private RegionRepository regionRepository;

    @Value("${insurance.premium.basis}")
    private int basis;

    @Autowired
    public CalculateService(RegionRepository regionRepository,
                            VehicleRepository vehicleRepository,
                            AnnoKilometersRepository annoKilometersRepository) {
        this.regionRepository = regionRepository;
        this.vehicleRepository = vehicleRepository;
        this.annoKilometersRepository = annoKilometersRepository;
    }

    public double calculateRegionFactor(String postcode) {
        Optional<Region> regionOpt = regionRepository.findByPostcodeValueStartingWith(postcode);

        if (regionOpt.isPresent()) {
            return regionOpt.get().getFactor();
        } else {
            logger.error("Region für Postleitzahl {} nicht gefunden", postcode);
            throw new RuntimeException("Region für Postleitzahl " + postcode + " nicht gefunden.");
        }
    }

    public double calculateVehicleFactor(Long vehicleId) {
        Optional<Vehicle> vehicleOpt = vehicleRepository.findById(vehicleId);

        if (vehicleOpt.isPresent()) {
            return vehicleOpt.get().getFactor();
        } else {
            logger.error("Fahrzeug mit ID {} nicht gefunden", vehicleId);
            throw new RuntimeException("Fahrzeug mit ID " + vehicleId + " nicht gefunden.");
        }
    }

    public double calculateAnnoKilometersFactor(int kmMin, int kmMax) {
        List<Anno_Kilometers> annoKilometers = annoKilometersRepository.findByMinLessThanEqualAndMaxGreaterThanEqual(kmMin, kmMax);

        if (!annoKilometers.isEmpty()) {
            return annoKilometers.get(0).getFactor();
        } else {
            logger.error("Kein Kilometerbereich für {} - {} km gefunden", kmMin, kmMax);
            throw new RuntimeException("Kein Kilometerbereich für " + kmMin + " - " + kmMax + " km gefunden.");
        }
    }

    public double calculatePremium(int kmMin, int kmMax, Long vehicleId, String postcode) {
        try {
            double f1 = calculateAnnoKilometersFactor(kmMin, kmMax);
            double f2 = calculateVehicleFactor(vehicleId);
            double f3 = calculateRegionFactor(postcode);

            double premiumFactor = f1 * f2 * f3;
            return premiumFactor * basis;
        } catch (RuntimeException ex) {
            logger.error("Fehler bei der Berechnung der Prämie: {}", ex.getMessage());
            throw ex;  // Exception weiterwerfen, um sie oben weiterzuleiten oder zu loggen
        }
    }
}
