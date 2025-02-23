package com.sina_reidenbach.InsurancePremium.controller;

import java.util.Comparator;
import com.sina_reidenbach.InsurancePremium.model.City;
import com.sina_reidenbach.InsurancePremium.model.Postcode;
import com.sina_reidenbach.InsurancePremium.model.Region;
import com.sina_reidenbach.InsurancePremium.model.Vehicle;
import com.sina_reidenbach.InsurancePremium.repository.CityRepository;
import com.sina_reidenbach.InsurancePremium.repository.PostcodeRepository;
import com.sina_reidenbach.InsurancePremium.repository.RegionRepository;
import com.sina_reidenbach.InsurancePremium.repository.VehicleRepository;
import com.sina_reidenbach.InsurancePremium.service.CalculateService;
import com.sina_reidenbach.InsurancePremium.service.LocationService;
import com.sina_reidenbach.InsurancePremium.service.VehicleService;
import com.sina_reidenbach.InsurancePremium.utils.IpUtils;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.sina_reidenbach.InsurancePremium.service.StatisticsService;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@Hidden
public class FrontendController {

    private static final Logger logger = LoggerFactory.getLogger(FrontendController.class);

    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private CalculateService calculateService;
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private PostcodeRepository postcodeRepository;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private StatisticsService statisticsService;

    private String handleError(Model model, String message) {
        model.addAttribute("error", message);
        logger.warn("[WARNUNG] {}", message);
        return showHomePage(null, model);
    }

    @PostMapping("/berechnen")
    public String berechnen(@RequestParam int km,
                            @RequestParam String postcodeValue,
                            @RequestParam Long vehicle,
                            HttpServletRequest request,
                            Model model) {

        Optional<Postcode> postcode = locationService.findPostcode(postcodeValue);
        if (postcode.isEmpty()) {
            handleError(model,"Bitte eine gültige Postleitzahl eingeben");
            return showHomePage(null, model);
        }

        Optional<City> selectedCity = locationService.findCityByPostcode(postcodeValue);
        if (selectedCity.isEmpty()) {
            handleError(model,"Internet Fehler aufgetreten");
            logger.warn("[WARNUNG] Keine Stadt zur angegebenen Postleitzahl: {}", postcodeValue);
            return showHomePage(null, model);
        }

        Optional<Region> selectedRegion = locationService.findRegionByCityName(selectedCity.get().getName());
        if (selectedRegion.isEmpty()) {
            handleError(model,"Internet Fehler aufgetreten");
            logger.warn("[WARNUNG] Keine Region zur angegebenen Stadt: {}", selectedCity.get().getName());
            return showHomePage(null, model);
        }

        String regionName = selectedRegion.get().getName();

        Vehicle selectedVehicle = vehicleRepository.findById(vehicle).orElse(null);

        LocalDateTime now = LocalDateTime.now();

        assert selectedVehicle != null;

        double premium;
        try {
            premium = calculateService.calculatePremium(km, km, vehicle, postcodeValue);
        } catch (Exception e) {
            handleError(model, "Fehler bei der Prämienberechnung: " + e.getMessage());
            logger.warn("[WARNUNG] Fehler bei der Prämienberechnung: {}", e.getMessage());
            return showHomePage(null, model);
        }

        String ipAddress = IpUtils.getClientIp(request);
        if (ipAddress != null && !ipAddress.trim().isEmpty()) {
            try {
                statisticsService.saveStatistics(now, postcodeValue, selectedVehicle.getName(), km, premium, ipAddress);
            } catch (Exception e) {
                logger.warn("[WARNUNG] Fehler beim Speichern der Statistik: {}", e.getMessage());
            }
        } else {
            logger.info("[INFO] Keine IP-Adresse gefunden. Statistik wird nicht gespeichert.");
        }

        List<Vehicle> vehicleList = vehicleService.getSortedVehicleList();
        if (vehicleList.isEmpty()) {
            handleError(model, "Interner Fehler aufgetreten");
            logger.warn("[WARNUNG] Keine Fahrzeuge in der Datenbank gefunden.");
            return showHomePage(null, model);
        }

        model.addAttribute("premium", String.format("%.2f", premium) + " €");
        model.addAttribute("region", regionName);
        model.addAttribute("vehicleList", vehicleList);

        return "index";
    }

    @GetMapping("/filter-postcodes")
    @ResponseBody
    public String filterPostcodes(@RequestParam String input) {
        if (input == null || input.trim().isEmpty()) {
            return "<option value=\"\">Keine Postleitzahl eingegeben</option>";
        }

        List<Postcode> filteredPostcodes = locationService.filterPostcodesByInput(input);
        if (filteredPostcodes.isEmpty()) {
            return "<option value=\"\">Keine Postleitzahlen gefunden</option>";
        }

        return filteredPostcodes.stream()
                .map(postcode -> "<option value=\"" + postcode.getPostcodeValue() + "\">" + postcode.getPostcodeValue() + "</option>")
                .collect(Collectors.joining());
    }


    @GetMapping("/")
    @Transactional
    public String showHomePage(@RequestParam(required = false) String input, Model model) {
        if (input == null) {
            input = "";
        }

        List<Postcode> postcodeList = postcodeRepository.findAll();

        postcodeList.sort(Comparator.comparing(Postcode::getPostcodeValue));

        List<Vehicle> vehicleList = vehicleRepository.findAll();

        vehicleList.sort(Comparator.comparing(Vehicle::getName));

        model.addAttribute("postcodeList", postcodeList);
        model.addAttribute("vehicleList", vehicleList);

        return "index";
    }
}
