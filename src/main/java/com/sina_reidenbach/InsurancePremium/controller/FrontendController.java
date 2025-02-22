package com.sina_reidenbach.InsurancePremium.controller;

import java.util.Collections;
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
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.sina_reidenbach.InsurancePremium.service.StatisticsService;
import java.time.LocalDateTime;
import org.springframework.web.bind.annotation.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Controller
public class FrontendController {

    private static final Logger logger = LoggerFactory.getLogger(FrontendController.class);

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

    @PostMapping("/berechnen")
    public String berechnen(@RequestParam int km,
                            @RequestParam String postcodeValue,
                            @RequestParam Long vehicle,
                            HttpServletRequest request,
                            Model model) {

        // Überprüfe, ob die Postleitzahl in der Datenbank existiert
        Optional<Postcode> postcode = postcodeRepository.findFirstByPostcodeValue(postcodeValue);
        if (postcode.isEmpty()) {
            model.addAttribute("errorMessage", "Bitte eine gültige Postleitzahl eingeben");
            return showHomePage(null, model); // Zurück zur Startseite mit Fehlermeldung
        }

        City selectedCity = cityRepository.findByPostcodes_PostcodeValue(postcodeValue);
        if (selectedCity == null) {
            String errorMessage = "Internet Fehler aufgetreten \"";
            model.addAttribute("error", errorMessage); // Fehlermeldung im Frontend
            logger.warn("[WARNUNG] {}: Keine Stadt zur angegebenen Postleitzahl: {}", errorMessage, postcodeValue); // Log im Logger
            return showHomePage(null, model); // Zurück zur Startseite mit Fehlermeldung
        }

        Region selectedRegion = regionRepository.findByCities_Name(selectedCity.getName());
        if (selectedRegion == null) {
            String errorMessage = "Internet Fehler aufgetreten \"";
            model.addAttribute("error", errorMessage); // Fehlermeldung im Frontend
            logger.warn("[WARNUNG] {}: Keine Region zur angegebenen Stadt: {}", errorMessage, selectedCity.getName()); // Log im Logger
            return showHomePage(null, model); // Zurück zur Startseite mit Fehlermeldung
        }

        String regionName = selectedRegion.getName();

        // Hole das Fahrzeug-Objekt aus der Datenbank anhand der ID
        Vehicle selectedVehicle = vehicleRepository.findById(vehicle).orElse(null);

        LocalDateTime now = LocalDateTime.now();

        // Speichern des Fahrzeugnamens (anstatt der ID) in der Statistik-Tabelle
        assert selectedVehicle != null;

        double premium;
        try {
            premium = calculateService.calculatePremium(km, km, vehicle, postcodeValue);
        } catch (Exception e) {
            model.addAttribute("error", "Fehler bei der Prämienberechnung: " + e.getMessage());
            logger.warn("[WARNUNG] Fehler bei der Prämienberechnung: {}", e.getMessage());
            return showHomePage(null, model); // Zurück zur Startseite mit Fehlermeldung
        }

        // IP-Adresse aus dem Request-Header extrahieren
        String ipAddress = request.getRemoteAddr();

        // Falls die IP hinter einem Proxy ist, prüfe den "X-Forwarded-For"-Header
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isEmpty()) {
            ipAddress = forwardedFor.split(",")[0]; // Erste IP aus der Liste
        }

        // Prüfe, ob eine gültige IP-Adresse vorhanden ist
        if (ipAddress != null && !ipAddress.trim().isEmpty()) {
            try {
                statisticsService.saveStatistics(now, postcodeValue, selectedVehicle.getName(), km, premium, ipAddress);
            } catch (Exception e) {
                logger.warn("[WARNUNG] Fehler beim Speichern der Statistik: {}", e.getMessage());
            }
        } else {
            logger.info("[INFO] Keine IP-Adresse gefunden. Statistik wird nicht gespeichert.");
        }

        List<Vehicle> vehicleList = vehicleRepository.findAll();
        if (vehicleList.isEmpty()) {
            String errorMessage = "Interner Fehler aufgetreten \"";
            model.addAttribute("error", errorMessage);
            logger.warn("[WARNUNG] {}: Keine Fahrzeuge in der Datenbank gefunden.", errorMessage); // Log im Logger

            return showHomePage(null, model); // Zurück zur Startseite mit Fehlermeldung
        }

        Collections.sort(vehicleList, Comparator.comparing(Vehicle::getName));

        // Werte ins Model packen
        model.addAttribute("premium", String.format("%.2f", premium) + " €"); // Formatierung mit zwei Nachkommastellen
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

        List<Postcode> filteredPostcodes = postcodeRepository.findAll().stream()
                .filter(postcode -> postcode.getPostcodeValue().startsWith(input))
                .toList();

        if (filteredPostcodes.isEmpty()) {
            return "<option value=\"\">Keine Postleitzahlen gefunden</option>";
        }

        StringBuilder optionsHtml = new StringBuilder();
        for (Postcode postcode : filteredPostcodes) {
            optionsHtml.append("<option value=\"").append(postcode.getPostcodeValue()).append("\">").append(postcode.getPostcodeValue()).append("</option>");
        }

        return optionsHtml.toString();
    }

    @GetMapping("/")
    @Transactional
    public String showHomePage(@RequestParam(required = false) String input, Model model) {
        // Falls kein Wert übergeben wird, leere das Eingabefeld
        if (input == null) {
            input = "";
        }

        List<Postcode> postcodeList = postcodeRepository.findAll();

        // Optional: Alphabetische Sortierung der PostcodeListe nach Postleitzahl
        postcodeList.sort(Comparator.comparing(Postcode::getPostcodeValue));

        List<Vehicle> vehicleList = vehicleRepository.findAll();

        // Alphabetische Sortierung der Fahrzeugliste nach Fahrzeugname
        vehicleList.sort(Comparator.comparing(Vehicle::getName));

        // Übergabe der PLZ-Liste und Fahrzeugliste an das Template
        model.addAttribute("postcodeList", postcodeList);
        model.addAttribute("vehicleList", vehicleList);

        return "index"; // Name der HTML-Datei in /templates/
    }
}
