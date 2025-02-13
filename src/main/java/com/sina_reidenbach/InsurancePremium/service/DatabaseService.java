package com.sina_reidenbach.InsurancePremium.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.sina_reidenbach.InsurancePremium.model.City;
import com.sina_reidenbach.InsurancePremium.model.Postcode;
import com.sina_reidenbach.InsurancePremium.model.Region;
import com.sina_reidenbach.InsurancePremium.repository.CityRepository;
import com.sina_reidenbach.InsurancePremium.repository.PostcodeRepository;
import com.sina_reidenbach.InsurancePremium.repository.RegionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Service
@Profile("dbinit-off") // Diese Klasse wird nur aktiv, wenn das Profil "dbinit" gesetzt ist
public class DatabaseService {
    @Autowired
    private ApplicationContext applicationContext;
    private final List<String[]> data = new ArrayList<>();

    @Autowired
    private PostcodeRepository postcodeRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private CityRepository cityRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        System.out.println("✅ DatabaseService gestartet!");
        readCSV();
        DatabaseService proxy = applicationContext.getBean(DatabaseService.class);
        proxy.saveDataTransactional(); // Aufruf über Proxy
    }

    private void readCSV() {
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
            System.out.println("❌ Fehler beim Lesen der Datei: " + e.getMessage());
        }
    }

    // Methode für Region abrufen oder erstellen
    @Transactional
    public Region findOrCreateRegion(String regionName) {
        Region region = regionRepository.findByName(regionName);
        if (region == null) {
            region = new Region();
            region.setName(regionName);
            region = entityManager.merge(region);  // Merge statt save wegen des EntityManagers
        }
        return region;
    }

    // Methode für City abrufen oder erstellen
    @Transactional
    public City findOrCreateCity(String cityName, Region region) {
        City city = cityRepository.findByName(cityName);
        if (city == null) {
            city = new City();
            city.setName(cityName);
            city.setRegion(region);  // Region zuweisen
            entityManager.persist(city);
        }
        return city;
    }

    // Methode für Postcode abrufen oder erstellen
    @Transactional
    public Postcode findOrCreatePostcode(String value, City city, Region region) {
        Postcode postcode = postcodeRepository.findByPostcodeValue(value);
        if (postcode == null) {
            postcode = new Postcode();
            postcode.setPostcodeValue(value);
            postcode.setCity(city);
            postcode.setRegion(region);
            entityManager.persist(postcode);
        }
        return postcode;
    }

    // Hauptmethode zum Speichern der Daten
    @Transactional
    public void saveDataTransactional() {
        try {
            for (String[] row : data) {
                String regionName = row[2].replace("\"", "").trim();
                String value = row[6].replace("\"", "").trim();;
                String cityName = row[7].replace("\"", "").trim();

                // Region abrufen oder neu erstellen
                Region region = findOrCreateRegion(regionName);

                // City erstellen und Region zuweisen
                City city = findOrCreateCity(cityName, region);

                // Postcode erstellen und Region zuweisen
                Postcode postcode = findOrCreatePostcode(value, city, region);

                // Weitere Logik kann hier nach Bedarf eingefügt werden
            }
        } catch (Exception e) {
            System.out.println("❌ Fehler beim Speichern der Daten: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
