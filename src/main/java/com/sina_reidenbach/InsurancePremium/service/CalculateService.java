package com.sina_reidenbach.InsurancePremium.service;

import com.sina_reidenbach.InsurancePremium.model.Anno_Kilometers;
import com.sina_reidenbach.InsurancePremium.model.Region;
import com.sina_reidenbach.InsurancePremium.model.Vehicle;
import com.sina_reidenbach.InsurancePremium.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class CalculateService {
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private AnnoKilometersRepository annoKilometersRepository;
    @Autowired
    private RegionRepository regionRepository;

    @Value("${insurance.premium.basis}")
    private int basis;
    double premium=0;
    @Autowired
    public CalculateService(RegionRepository regionRepository,
                            VehicleRepository vehicleRepository,
                            AnnoKilometersRepository annoKilometersRepository) {
        this.regionRepository = regionRepository;
        this.vehicleRepository = vehicleRepository;
        this.annoKilometersRepository = annoKilometersRepository;
    }

    public double calculateRegionFactor(String postcode) {
        // Verwende eine AtomicReference, um den Wert von 'regionFactor' innerhalb des Lambdas zu ändern
        AtomicReference<Double> regionFactor = new AtomicReference<>(0.0); // Standardwert 1.0

        Optional<Region> regionOpt = regionRepository.findByPostcodeValueStartingWith(postcode);

        // Wenn eine Region vorhanden ist, den Faktor setzen
        regionOpt.ifPresent(region -> {
            regionFactor.set(region.getFactor());
        });
        return regionFactor.get();
    }

    public double calculateVehicleFactor(Long vehicleId) {
        // Verwende eine AtomicReference, um den Wert von 'vehicleFactor' innerhalb des Lambdas zu ändern
        AtomicReference<Double> vehicleFactor = new AtomicReference<>(0.0); // Standardwert 1.0

        Optional<Vehicle> vehicleOpt = vehicleRepository.findById(vehicleId);

        // Wenn ein Vehicle vorhanden ist, den Faktor setzen
        vehicleOpt.ifPresent(vehicle -> {
            vehicleFactor.set(vehicle.getFactor());
        });
        return vehicleFactor.get();
    }

    public double calculateAnnoKilometersFactor(int kmMin, int kmMax) {
        List<Anno_Kilometers> annoKilometers = annoKilometersRepository.findByMinLessThanEqualAndMaxGreaterThanEqual(kmMin,kmMax);

        double kmFactor = annoKilometers.get(0).getFactor();

        return kmFactor;
    }


    public double calculatePremium(int kmMin, int kmMax, Long vehicleId, String postcode) {
        double f1 = calculateAnnoKilometersFactor(kmMin,kmMax);
        double f2 = calculateVehicleFactor(vehicleId);
        double f3 = calculateRegionFactor(postcode);

        double premiumFactor = f1*f2*f3;
        premium = premiumFactor * basis;

        return premium;
    }
}
        //HTML-API für Drittanbieter
        //jährliche IP Löschung und Speichern in Archiv Jahr



