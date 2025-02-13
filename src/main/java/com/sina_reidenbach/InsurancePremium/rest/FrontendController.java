package com.sina_reidenbach.InsurancePremium.rest;

import java.util.Collections;
import java.util.Comparator;

import com.sina_reidenbach.InsurancePremium.model.Postcode;
import com.sina_reidenbach.InsurancePremium.model.Region;
import com.sina_reidenbach.InsurancePremium.model.Vehicle;
import com.sina_reidenbach.InsurancePremium.repository.PostcodeRepository;
import com.sina_reidenbach.InsurancePremium.repository.RegionRepository;
import com.sina_reidenbach.InsurancePremium.repository.VehicleRepository;
import com.sina_reidenbach.InsurancePremium.service.CalculateService;
import com.sina_reidenbach.InsurancePremium.service.PostcodeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.stream.Collectors;
import com.sina_reidenbach.InsurancePremium.service.StatisticsService;
import java.time.LocalDateTime;


import java.util.List;

@Controller
public class FrontendController {
    @Autowired
    private CalculateService calculateService;
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private PostcodeRepository postcodeRepository;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private PostcodeService postcodeService; // Hinzufügen des Services
    @Autowired
    private StatisticsService statisticsService;

    @PostMapping("/berechnen")
    public String berechnen(@RequestParam int km,
                            @RequestParam String postcodeValue,
                            @RequestParam Long vehicle,
                            HttpServletRequest request,
                            Model model) {


        // Hole das Region-Objekt aus der Datenbank anhand der Postleitzahl
        Region selectedRegion = regionRepository.findByPostcodeValue(postcodeValue);
        String regionName = selectedRegion.getName();

        // Hole das Fahrzeug-Objekt aus der Datenbank anhand der ID
        Vehicle selectedVehicle = vehicleRepository.findById(vehicle).orElse(null);


        LocalDateTime now = LocalDateTime.now();
        System.out.println("Aktuelles Datum und Uhrzeit: " + now);


        // Speichern des Fahrzeugnamens (anstatt der ID) in der Statistik-Tabelle
        String vehicleName = selectedVehicle.getName();
        double premium = calculateService.calculatePremium(km, postcodeValue, String.valueOf(vehicle));
        // IP-Adresse aus dem Request-Header extrahieren
        String ipAddress = request.getRemoteAddr();

        // Falls die IP hinter einem Proxy ist, die "X-Forwarded-For"-Header prüfen
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isEmpty()) {
            ipAddress = forwardedFor.split(",")[0]; // Nimmt die erste IP aus der Liste
        }
        statisticsService.saveStatistics(now, postcodeValue, vehicleName, km, premium, ipAddress);



        List<Vehicle> fahrzeugListe = vehicleRepository.findAll();

        // Alphabetische Sortierung der Fahrzeugliste nach Fahrzeugname
        Collections.sort(fahrzeugListe, Comparator.comparing(Vehicle::getName));

        // Werte ins Model packen
        model.addAttribute("premium", String.format("%.2f", premium) + " €"); // Formatierung mit zwei Nachkommastellen
        model.addAttribute("region", regionName);
        model.addAttribute("fahrzeugListe", fahrzeugListe);

        return "index";
    }




    @GetMapping("/")
    public String showHomePage(Model model) {
        // Hole alle Postcode Objekte aus der Datenbank
        List<Postcode> postcodeList = postcodeRepository.findAll();

        List<Vehicle> vehicleList = vehicleRepository.findAll();

        // Alphabetische Sortierung der Fahrzeugliste nach Fahrzeugname
        Collections.sort(vehicleList, Comparator.comparing(Vehicle::getName));

        // Alphabetische Sortierung der PostcodeListe nach Fahrzeugname
        Collections.sort(postcodeList, Comparator.comparing(Postcode::getPostcodeValue));


        // Übergabe der PLZ-Liste an das Template
        model.addAttribute("postcodeList", postcodeList);
        model.addAttribute("vehicleList", vehicleList);

        return "index"; // Name der HTML-Datei in /templates/
    }
}