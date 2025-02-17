package com.sina_reidenbach.InsurancePremium.rest;

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


import java.util.List;
import java.util.stream.Collectors;

@Controller
public class FrontendController {
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


        // Hole das Region-Objekt aus der Datenbank anhand der Postleitzahl
        City selectedCity = cityRepository.findByPostcodes_PostcodeValue(postcodeValue);
        Region selectedRegion = regionRepository.findByCities_Name(selectedCity.getName());
        String regionName = selectedRegion.getName();

        // Hole das Fahrzeug-Objekt aus der Datenbank anhand der ID
        Vehicle selectedVehicle = vehicleRepository.findById(vehicle).orElse(null);


        LocalDateTime now = LocalDateTime.now();
        System.out.println("Aktuelles Datum und Uhrzeit: " + now);


        // Speichern des Fahrzeugnamens (anstatt der ID) in der Statistik-Tabelle
        String vehicleName = selectedVehicle.getName();

        double premium = calculateService.calculatePremium(km, km, vehicle, postcodeValue);
        // IP-Adresse aus dem Request-Header extrahieren
        String ipAddress = request.getRemoteAddr();

        // Falls die IP hinter einem Proxy ist, die "X-Forwarded-For"-Header prüfen
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isEmpty()) {
            ipAddress = forwardedFor.split(",")[0]; // Nimmt die erste IP aus der Liste
        }
        statisticsService.saveStatistics(now, postcodeValue, vehicleName, km, premium, ipAddress);


        List<Vehicle> vehicleList = vehicleRepository.findAll();

        // Alphabetische Sortierung der Fahrzeugliste nach Fahrzeugname
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
        List<Postcode> filteredPostcodes = postcodeRepository.findAll().stream()
                .filter(postcode -> postcode.getPostcodeValue().startsWith(input))
                .collect(Collectors.toList());

        StringBuilder optionsHtml = new StringBuilder();
        for (Postcode postcode : filteredPostcodes) {
            optionsHtml.append("<option value=\"" + postcode.getPostcodeValue() + "\">" + postcode.getPostcodeValue() + "</option>");
        }

        return optionsHtml.toString(); // Gibt die Optionen als HTML zurück
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
        Collections.sort(postcodeList, Comparator.comparing(Postcode::getPostcodeValue));


        List<Vehicle> vehicleList = vehicleRepository.findAll();

        // Alphabetische Sortierung der Fahrzeugliste nach Fahrzeugname
        Collections.sort(vehicleList, Comparator.comparing(Vehicle::getName));

        // Übergabe der PLZ-Liste und Fahrzeugliste an das Template
        model.addAttribute("postcodeList", postcodeList);
        model.addAttribute("vehicleList", vehicleList);

        return "index"; // Name der HTML-Datei in /templates/
    }
}