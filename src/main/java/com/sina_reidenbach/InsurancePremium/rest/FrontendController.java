package com.sina_reidenbach.InsurancePremium.rest;

import java.util.Collections;
import java.util.Comparator;
import com.sina_reidenbach.InsurancePremium.model.Region;
import com.sina_reidenbach.InsurancePremium.model.Vehicle;
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
import java.time.format.DateTimeFormatter;


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
    @Autowired
    private StatisticsService statisticsService;

    @PostMapping("/berechnen")
    public String berechnen(@RequestParam int km,
                            @RequestParam String postcode,
                            @RequestParam int vehicle,
                            HttpServletRequest request,
                            Model model) {

        // Hole das Fahrzeug-Objekt aus der Datenbank anhand der ID
        Vehicle selectedVehicle = vehicleRepository.findById(vehicle).orElse(null);

        // Wenn das Fahrzeug nicht gefunden wurde, kann eine Fehlerbehandlung erfolgen
        if (selectedVehicle == null) {
            model.addAttribute("error", "Fahrzeug nicht gefunden!");
            return "index";
        }

        LocalDateTime now = LocalDateTime.now();
        System.out.println("Aktuelles Datum und Uhrzeit: " + now);


        // Speichern des Fahrzeugnamens (anstatt der ID) in der Statistik-Tabelle
        String vehicleName = selectedVehicle.getVehicleName();
        double premium = calculateService.calculatePremium(km, postcode, String.valueOf(vehicle));
        // IP-Adresse aus dem Request-Header extrahieren
        String ipAddress = request.getRemoteAddr();

        // Falls die IP hinter einem Proxy ist, die "X-Forwarded-For"-Header prüfen
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isEmpty()) {
            ipAddress = forwardedFor.split(",")[0]; // Nimmt die erste IP aus der Liste
        }
        statisticsService.saveStatistics(now, postcode, vehicleName, km, premium, ipAddress);


        // Region anhand der Postleitzahl holen
        String regionName = postcodeService.getRegionByPostcode(postcode);


        List<Vehicle> fahrzeugListe = vehicleRepository.findAll();

        // Alphabetische Sortierung der Fahrzeugliste nach Fahrzeugname
        Collections.sort(fahrzeugListe, Comparator.comparing(Vehicle::getVehicleName));

        // Werte ins Model packen
        model.addAttribute("premium", String.format("%.2f", premium) + " €"); // Formatierung mit zwei Nachkommastellen
        model.addAttribute("region", regionName);
        model.addAttribute("fahrzeugListe", fahrzeugListe);

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