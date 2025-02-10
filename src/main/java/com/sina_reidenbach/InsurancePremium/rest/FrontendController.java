package com.sina_reidenbach.InsurancePremium.rest;

import java.util.Collections;
import java.util.Comparator;
import com.sina_reidenbach.InsurancePremium.model.Region;
import com.sina_reidenbach.InsurancePremium.model.Vehicle;
import com.sina_reidenbach.InsurancePremium.repository.RegionRepository;
import com.sina_reidenbach.InsurancePremium.repository.VehicleRepository;
import com.sina_reidenbach.InsurancePremium.service.CalculateService;
import com.sina_reidenbach.InsurancePremium.service.PostcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.stream.Collectors;


import java.util.List;

@Controller
public class FrontendController {
    @Autowired
    private CalculateService calculateService;
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private PostcodeService postcodeService; // Hinzufügen des Services

    @PostMapping("/berechnen")
    public String berechnen(@RequestParam int km,
                            @RequestParam String postcode,
                            @RequestParam String vehicle,
                            Model model) {
        // Region anhand der Postleitzahl holen
        String regionName = postcodeService.getRegionByPostcode(postcode);

        // Prämie berechnen
        double premium = calculateService.calculatePremium(km, postcode, vehicle);

        // Werte ins Model packen
        model.addAttribute("premium", String.format("%.2f", premium) + " €"); // Formatierung mit zwei Nachkommastellen
        model.addAttribute("region", regionName);
        model.addAttribute("fahrzeugListe", vehicleRepository.findAll());

        return "index";
    }




    @GetMapping("/")
    public String showHomePage(Model model) {
        // Hole alle Regionen (mit PLZ) aus der Datenbank
        List<Region> plzListe = regionRepository.findAll();

        // Extrahiere nur die Postleitzahlen aus den Regionen
        List<String> postcodeList = plzListe.stream()
                .map(Region::getPostcode) // Nur die PLZ
                .collect(Collectors.toList());

        List<Vehicle> fahrzeugListe = vehicleRepository.findAll();

        // Alphabetische Sortierung der Fahrzeugliste nach Fahrzeugname
        Collections.sort(fahrzeugListe, Comparator.comparing(Vehicle::getVehicleName));


        System.err.println("PLZ Liste Größe: " + plzListe.size());
        // Übergabe der PLZ-Liste an das Template
        model.addAttribute("postcodeList", postcodeList);
        model.addAttribute("fahrzeugListe", fahrzeugListe);

        return "index"; // Name der HTML-Datei in /templates/
    }
}