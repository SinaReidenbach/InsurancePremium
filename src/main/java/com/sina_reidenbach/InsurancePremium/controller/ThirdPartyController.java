package com.sina_reidenbach.InsurancePremium.controller;

import com.sina_reidenbach.InsurancePremium.dto.AnnoKilometersResponse;
import com.sina_reidenbach.InsurancePremium.dto.PremiumResponse;
import com.sina_reidenbach.InsurancePremium.dto.RegionResponse;
import com.sina_reidenbach.InsurancePremium.dto.VehicleResponse;
import com.sina_reidenbach.InsurancePremium.model.*;
import com.sina_reidenbach.InsurancePremium.repository.*;
import com.sina_reidenbach.InsurancePremium.service.CalculateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;


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

    @GetMapping("/api/options/vehicles")
    public VehicleResponse getVehicles() {
        VehicleResponse response = new VehicleResponse();
        List<Vehicle> vehicles = vehicleRepository.findAll();
        Map<Long, Map<String, Object>> vehicleMap = new HashMap<>();
        for (Vehicle vehicle : vehicles) {
            Map<String, Object> vehicleData = new HashMap<>();
            vehicleData.put("vehicleName", vehicle.getName());
            vehicleData.put("factor", vehicle.getFactor());
            vehicleMap.put(vehicle.getId(), vehicleData);
        }
        response.setVehicles(vehicleMap);
        return response;
    }

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

        @PostMapping("/api/calculate")
        public PremiumResponse calculatePremium(@RequestBody Map<String, Object> premiumRequest) {
            PremiumResponse response = new PremiumResponse();

            try {
                Number vehicleIdNumber = (Number) premiumRequest.get("vehicleId");
                Long vehicleId = vehicleIdNumber.longValue();
                int annoKilometers = (int) premiumRequest.get("annoKilometers");
                String postcode = (String) premiumRequest.get("postcode");

                // Prämie berechnen
                double premiumAmount= calculateService.calculatePremium(annoKilometers,annoKilometers,vehicleId,postcode);

                // Prämie in der Antwort setzen
                Map<String, Object> premium = new HashMap<>();
                premium.put("premium", premiumAmount);
                response.setPremium(premium);

            } catch (Exception e) {
                response.setPremium(null);  // Setze null als Fallback
            }

            return response;
        }
    }
