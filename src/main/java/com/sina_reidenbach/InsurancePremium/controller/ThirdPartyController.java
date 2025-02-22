package com.sina_reidenbach.InsurancePremium.controller;

import com.sina_reidenbach.InsurancePremium.dto.*;
import com.sina_reidenbach.InsurancePremium.model.*;
import com.sina_reidenbach.InsurancePremium.repository.*;
import com.sina_reidenbach.InsurancePremium.service.CalculateService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.StreamingHttpOutputMessage;
import org.springframework.web.bind.annotation.*;

import java.sql.ParameterMetaData;
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

    @Operation(
            tags = "Fahrzeugtypen",
            description = "Gibt mögliche Optionen für die Fahrzeugtypen aus und die entsprechenden Prämien Faktoren für die Berechnung")
    @GetMapping("/api/options/vehicles")
    public ResponseEntity<VehicleResponse> getVehicles() {
        VehicleResponse response = new VehicleResponse();
        try {
            List<Vehicle> vehicles = vehicleRepository.findAll();
            if (vehicles.isEmpty()) {
                Map<Long, Map<String, Object>> errorMap = new HashMap<>();
                Map<String, Object> errorData = new HashMap<>();
                errorData.put("error", "NO_VEHICLES_FOUND");
                errorData.put("message", "Keine Fahrzeuge gefunden.");
                errorMap.put(0L, errorData);
                response.setVehicles(errorMap);
                return ResponseEntity.status(404).body(response);
            }

            Map<Long, Map<String, Object>> vehicleMap = new HashMap<>();
            for (Vehicle vehicle : vehicles) {
                Map<String, Object> vehicleData = new HashMap<>();
                vehicleData.put("vehicleName", vehicle.getName());
                vehicleData.put("factor", vehicle.getFactor());
                vehicleMap.put(vehicle.getId(), vehicleData);
            }
            response.setVehicles(vehicleMap);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<Long, Map<String, Object>> errorMap = new HashMap<>();
            Map<String, Object> errorData = new HashMap<>();
            errorData.put("error", "INTERNAL_SERVER_ERROR");
            errorData.put("message", "Ein unerwarteter Fehler ist aufgetreten.");
            errorMap.put(0L, errorData);
            response.setVehicles(errorMap);
            return ResponseEntity.status(500).body(response);
        }
    }

    @Operation(
            tags = "Ansässigkeit Zulassungsstelle",
            description = "Gibt mögliche Optionen für die Regionen der Zulassungsstellen aus und die entsprechenden Prämien Faktoren für die Berechnung")
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

    @Operation(summary = "Gibt die Optionen für die Kilometer Ranges aus und die entsprechenden Prämien Faktoren für die Berechnung")
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

    @Operation(
            tags = "Prämienberechnung",
            description = "Berechnet die Prämie über die gesendeten Parameter Vehicle, Postcode und annoKilometers",
            responses = {
                    @ApiResponse(
                            description = "Prämienberechnung erfolgt und zurückgesendet",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(name = "Beispiel", value = "{\"vehicleTypeId\": 14, \"postcode\": \"67890\", \"annoKilometers\": 5000}")
                                    },
                                    schema = @Schema(implementation = PremiumResponse.class)
                            )
                    )
            }
    )
    @PostMapping("/api/calculate")
    public ResponseEntity<?> calculatePremium(@org.springframework.web.bind.annotation.
            RequestBody Map<String, Object> premiumRequest) {

        List<String> errorMessages = new ArrayList<>();
        PremiumResponse response = new PremiumResponse();

        try {
            Number vehicleIdNumber = (Number) premiumRequest.get("vehicleId");
            Long vehicleId = vehicleIdNumber != null ? vehicleIdNumber.longValue() : null;
            Integer annoKilometers = (Integer) premiumRequest.get("annoKilometers");
            String postcode = (String) premiumRequest.get("postcode");

            if (vehicleId == null) errorMessages.add("vehicleId muss angegeben werden.");
            if (annoKilometers == null) errorMessages.add("annoKilometers muss angegeben werden.");
            if (postcode == null) errorMessages.add("postcode muss angegeben werden.");

            if (vehicleId != null && vehicleRepository.findById(vehicleId).isEmpty()) {
                errorMessages.add("Kein Fahrzeug mit der angegebenen ID gefunden.");
            }

            if (postcode != null && postcodeRepository.findFirstByPostcodeValue(postcode).isEmpty()) {
                errorMessages.add("Kein Postleitzahl-Eintrag für den angegebenen Wert gefunden.");
            }

            if (annoKilometers != null && annoKilometers <= 0) {
                errorMessages.add("Kilometerzahl muss größer als 0 sein.");
            }

            if (!errorMessages.isEmpty()) {
                return ResponseEntity.badRequest().body(new ErrorResponse("Fehlerhafte Eingabewerte", String.join(", ", errorMessages)));
            }

            double premiumAmount = calculateService.calculatePremium(annoKilometers, annoKilometers, vehicleId, postcode);

            Map<String, Object> premium = new HashMap<>();
            premium.put("premium", premiumAmount);
            response.setPremium(premium);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("Serverfehler", "Ein unerwarteter Fehler ist aufgetreten"));
        }
    }

}