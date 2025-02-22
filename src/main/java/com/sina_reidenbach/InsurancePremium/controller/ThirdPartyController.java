package com.sina_reidenbach.InsurancePremium.controller;

import com.sina_reidenbach.InsurancePremium.dto.*;
import com.sina_reidenbach.InsurancePremium.model.*;
import com.sina_reidenbach.InsurancePremium.repository.*;
import com.sina_reidenbach.InsurancePremium.service.CalculateService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ThirdPartyController {

    private static final Logger logger = LoggerFactory.getLogger(ThirdPartyController.class);

    @Autowired
    private CalculateService calculateService;
    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private AnnoKilometersRepository annoKilometersRepository;

    @Autowired
    private PostcodeRepository postcodeRepository;

    @Operation(summary = "Gibt mögliche Optionen für die Fahrzeugtypen aus")
    @GetMapping("/api/options/vehicles")
    public ResponseEntity<VehicleResponse> getVehicles() {
        VehicleResponse response = new VehicleResponse();
        try {
            List<Vehicle> vehicles = vehicleRepository.findAll();
            if (vehicles.isEmpty()) {
                // Fehlerhafte Antwort mit der richtigen Struktur
                Map<Long, Map<String, Object>> errorMap = new HashMap<>();
                Map<String, Object> errorData = new HashMap<>();
                errorData.put("error", "NO_VEHICLES_FOUND");
                errorData.put("message", "Keine Fahrzeuge gefunden.");
                errorMap.put(0L, errorData);  // 0L als Dummy-Id für den Fehler
                response.setVehicles(errorMap);
                return ResponseEntity.status(404).body(response);  // 404 Not Found
            }

            // Erfolgreiche Antwort mit Fahrzeugen
            Map<Long, Map<String, Object>> vehicleMap = new HashMap<>();
            for (Vehicle vehicle : vehicles) {
                Map<String, Object> vehicleData = new HashMap<>();
                vehicleData.put("vehicleName", vehicle.getName());
                vehicleData.put("factor", vehicle.getFactor());
                vehicleMap.put(vehicle.getId(), vehicleData);
            }
            response.setVehicles(vehicleMap);

            return ResponseEntity.ok(response);  // Erfolgreiche Antwort

        } catch (Exception e) {
            // Allgemeiner Fehler, auch hier die korrekte Struktur für den Fehler verwenden
            Map<Long, Map<String, Object>> errorMap = new HashMap<>();
            Map<String, Object> errorData = new HashMap<>();
            errorData.put("error", "INTERNAL_SERVER_ERROR");
            errorData.put("message", "Ein unerwarteter Fehler ist aufgetreten.");
            errorMap.put(0L, errorData);  // Dummy-Id für Fehler
            response.setVehicles(errorMap);
            return ResponseEntity.status(500).body(response);  // 500 Internal Server Error
        }
    }

    @Operation(summary = "Gibt mögliche Optionen für die Regionen der Zulassungsstellen aus")
    @GetMapping("/api/options/regions")
    public RegionResponse getRegions() {
        RegionResponse response = new RegionResponse();
        List<Region> regions = regionRepository.findAll();
        Map<Long, Map<String, Object>> regionMap = new HashMap<>();
        for (Region region : regions) {
            Map<String, Object> regionData = new HashMap<>();
            regionData.put("regionName", region.getName());
            regionData.put("factor", region.getFactor());
            regionMap.put(region.getId(), regionData);
        }
        response.setRegions(regionMap);
        return response;
    }

    @Operation(summary = "Gibt die Faktorbereiche für die Kilometer Ranges aus")
    @GetMapping("/api/options/annoKilometers")
    public AnnoKilometersResponse getAnnoKilometers() {
        AnnoKilometersResponse response = new AnnoKilometersResponse();
        List<Anno_Kilometers> kmFactors = annoKilometersRepository.findAll();
        List<Map<String, Object>> kmList = new ArrayList<>();
        for (Anno_Kilometers kmFactor : kmFactors) {
            Map<String, Object> kmData = new HashMap<>();
            kmData.put("min", kmFactor.getMin());
            kmData.put("max", kmFactor.getMax());
            kmData.put("factor", kmFactor.getFactor());
            kmList.add(kmData);
        }
        response.setAnnoKilometers(kmList);
        return response;
    }

    @Operation(summary = "Berechnet die Prämie mit den entsprechenden Optionen")
    @PostMapping("/api/calculate")
    public ResponseEntity<?> calculatePremium(@org.springframework.web.bind.annotation.RequestBody Map<String, Object> premiumRequest) {

        List<String> errorMessages = new ArrayList<>();
        PremiumResponse response = new PremiumResponse();

        try {
            // Eingabewerte extrahieren
            Number vehicleIdNumber = (Number) premiumRequest.get("vehicleId");
            Long vehicleId = vehicleIdNumber != null ? vehicleIdNumber.longValue() : null;
            Integer annoKilometers = (Integer) premiumRequest.get("annoKilometers");
            String postcode = (String) premiumRequest.get("postcode");

            // Eingaben validieren
            if (vehicleId == null) errorMessages.add("vehicleId muss angegeben werden.");
            if (annoKilometers == null) errorMessages.add("annoKilometers muss angegeben werden.");
            if (postcode == null) errorMessages.add("postcode muss angegeben werden.");

            // Fahrzeug prüfen
            if (vehicleId != null && vehicleRepository.findById(vehicleId).isEmpty()) {
                errorMessages.add("Kein Fahrzeug mit der angegebenen ID gefunden.");
            }

            // Postleitzahl prüfen
            if (postcode != null && postcodeRepository.findFirstByPostcodeValue(postcode).isEmpty()) {
                errorMessages.add("Kein Postleitzahl-Eintrag für den angegebenen Wert gefunden.");
            }

            // Kilometer validieren
            if (annoKilometers != null && annoKilometers <= 0) {
                errorMessages.add("Kilometerzahl muss größer als 0 sein.");
            }

            // Falls Fehler vorhanden sind, gib alle zurück
            if (!errorMessages.isEmpty()) {
                return ResponseEntity.badRequest().body(new ErrorResponse("Fehlerhafte Eingabewerte", String.join(", ", errorMessages)));
            }

            // Prämie berechnen
            double premiumAmount = calculateService.calculatePremium(annoKilometers, annoKilometers, vehicleId, postcode);

            // Antwort erstellen
            Map<String, Object> premium = new HashMap<>();
            premium.put("premium", premiumAmount);
            response.setPremium(premium);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("Serverfehler", "Ein unerwarteter Fehler ist aufgetreten"));
        }
    }

}