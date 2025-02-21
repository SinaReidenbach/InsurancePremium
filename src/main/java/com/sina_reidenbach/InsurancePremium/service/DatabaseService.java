package com.sina_reidenbach.InsurancePremium.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.sina_reidenbach.InsurancePremium.model.*;
import com.sina_reidenbach.InsurancePremium.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

@Service
public class DatabaseService {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseService.class);

    @Autowired
    private ApplicationContext applicationContext;

    private final List<String[]> data = new ArrayList<>();

    @Autowired
    private AnnoKilometersRepository annoKilometersRepository;
    @Autowired
    private PostcodeRepository postcodeRepository;
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private VehicleRepository vehicleRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        logger.info("✅ DatabaseService gestartet!");
        readCSV();
        DatabaseService proxy = applicationContext.getBean(DatabaseService.class);
        proxy.saveDataTransactional(); // Aufruf über Proxy
    }

    public void readCSV() {
        try (Reader reader = new FileReader(new ClassPathResource("postcodes.csv").getFile());
             CSVReader csvReader = new CSVReader(reader)) {

            List<String[]> rows = csvReader.readAll();
            boolean firstLine = true;

            for (String[] values : rows) {
                if (firstLine) {
                    firstLine = false;
                    continue; // Header überspringen
                }
                if (values.length > 7) { // Prüfen, ob genügend Spalten existieren
                    data.add(values);
                }
            }
        } catch (IOException | CsvException e) {
            logger.error("❌ Fehler beim Lesen der Datei: {}", e.getMessage());
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Anno_Kilometers> createAnno_Kilometers() {
        List<Anno_Kilometers> annoKilometersList = Arrays.asList(
                new Anno_Kilometers(0, 5000, 0.5),
                new Anno_Kilometers(5001, 10000, 1.0),
                new Anno_Kilometers(10001, 20000, 1.5),
                new Anno_Kilometers(20001, Integer.MAX_VALUE, 2.0));

        for (Anno_Kilometers annoKilometers : annoKilometersList) {
            entityManager.persist(annoKilometers);
            entityManager.flush();
            entityManager.clear();
        }
        return annoKilometersList;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Vehicle> createVehicle() {
        List<Vehicle> vehicleList = Arrays.asList(
                new Vehicle("Pkw Kraftstoff", 1.5),
                new Vehicle("Lkw ohne Anhänger", 1.5),
                new Vehicle("Motorrad", 2.0),
                new Vehicle("Fahrrad", 1.5),
                new Vehicle("Bus", 1.0),
                new Vehicle("Traktor", 0.5),
                new Vehicle("E-Scooter", 1.5),
                new Vehicle("Roller (Motor)", 2.0),
                new Vehicle("PKW Elektro", 1.0),
                new Vehicle("Wohnmobil", 1.0),
                new Vehicle("Taxi", 1.5),
                new Vehicle("Transporter", 1.5),
                new Vehicle("Lkw mit Anhänger", 2.0),
                new Vehicle("Geländewagen (SUV)", 1.5),
                new Vehicle("Moped", 2.0));

        for (Vehicle vehicle : vehicleList) {
            entityManager.persist(vehicle);
            entityManager.flush();
            entityManager.clear();
        }
        return vehicleList;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Region> createRegion() {

        List<Region> regionList = Arrays.asList(
                new Region("Baden-Württemberg", 1.5),
                new Region("Bayern", 1.5),
                new Region("Berlin", 2.0),
                new Region("Brandenburg", 1.0),
                new Region("Bremen", 1.5),
                new Region("Hamburg", 1.5),
                new Region("Hessen", 1.0),
                new Region("Mecklenburg-Vorpommern", 0.5),
                new Region("Niedersachsen", 1.0),
                new Region("Nordrhein-Westfalen", 1.5),
                new Region("Rheinland-Pfalz", 1.0),
                new Region("Saarland", 1.0),
                new Region("Sachsen", 1.0),
                new Region("Sachsen-Anhalt", 0.5),
                new Region("Schleswig-Holstein", 0.5),
                new Region("Thüringen", 1.0));

        for (Region region : regionList) {
            entityManager.persist(region);
            entityManager.flush();
            entityManager.clear();
        }
        long count = (long) entityManager.createQuery("SELECT COUNT(r) FROM Region r").getSingleResult();

        return regionList;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public City createCity(String cityName, Region region) {
        return cityRepository.findFirstByName(cityName)
                .orElseGet(() -> {
                    City city = new City();
                    city.setName(cityName);
                    city.setRegion(region);
                    entityManager.persist(city);
                    entityManager.flush();
                    entityManager.clear();
                    return city;
                });
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Postcode createPostcode(String value, City city, Region region) {
        return postcodeRepository.findFirstByPostcodeValue(value)
                .orElseGet(() -> {
                    Postcode postcode = new Postcode();
                    postcode.setPostcodeValue(value);
                    postcode.setCity(city);
                    postcode.setRegion(region);
                    entityManager.persist(postcode);
                    entityManager.flush();
                    entityManager.clear();
                    return postcode;
                });
    }

    @Transactional(noRollbackFor = Exception.class)
    public void saveDataTransactional() {

        try {
            if (data.isEmpty()) {
                logger.warn("⚠️ Keine Daten zum Speichern!");
                return;
            }

            List<Anno_Kilometers> annoKilometersList = createAnno_Kilometers();
            logger.info("🚀 Tabelle Anno_kilometers wurde erstellt und befüllt");
            List<Vehicle> vehicleList = createVehicle();
            logger.info("🚀 Tabelle Vehicle wurde erstellt und befüllt");
            List<Region> regionList = createRegion();
            logger.info("🚀 Tabelle Region wurde erstellt und befüllt");
            logger.info("🚀 einen Moment bitte....");

            for (String[] row : data) {
                String regionName = row[2].replace("\"", "").trim();
                String value = row[6].replace("\"", "").trim();
                String cityName = row[7].replace("\"", "").trim();

                // Überprüfen, ob die Postleitzahl leer oder ungültig ist
                if (value.length() != 5 || !value.matches("\\d{5}")) { // Nur 5-stellige PLZ
                    logger.warn("⚠️ Ungültige oder fehlende Postleitzahl: {}", value);
                }

                // Überprüfen, ob Stadt und Region ebenfalls fehlen
                if (regionName.isEmpty() || cityName.isEmpty()) {
                    logger.warn("⚠️ Fehlende Region oder Stadt für Postleitzahl: {}", value);
                }

                // Wenn alles gültig ist, die Region, Stadt und Postleitzahl speichern
                if (!regionName.isEmpty() && !cityName.isEmpty() && !value.isEmpty() && value.matches("\\d{5}")) {
                    Optional<Region> optionalRegion = regionRepository.findByName(regionName);
                    if (optionalRegion.isEmpty()) {
                        logger.warn("⚠️ Region nicht gefunden: {}", regionName);
                        continue; // Falls die Region nicht existiert, wird dieser Datensatz übersprungen
                    }
                    Region region = optionalRegion.get();
                    City city = createCity(cityName, region);
                    Postcode postcode = createPostcode(value, city, region);
                }
            }
            logger.info("🚀 Tabellen Postcode und City wurden erstellt und befüllt");
            entityManager.flush();
            entityManager.clear();

            logger.info("✅ Alle Daten wurden erfolgreich gespeichert!");

        } catch (Exception e) {
            logger.error("❌ Fehler beim Speichern der Daten: {}", e.getMessage(), e);
        }
    }
}
