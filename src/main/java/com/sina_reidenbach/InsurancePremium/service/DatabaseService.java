package com.sina_reidenbach.InsurancePremium.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class DatabaseService {

    private final List<String[]> data = new ArrayList<>();

    @PostConstruct
    public void init() {
        System.out.println("✅ DatabaseService gestartet!");
        readCSV();
        printData();
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

    private void printData() {
        for (String[] row : data) {
            String regionName = row[2].trim();   // Feld 2
            String postcodeValue = row[6].trim(); // Feld 6
            String cityName = row[7].trim();    // Feld 7

            System.out.println("Region: " + regionName + ", Postcode: " + postcodeValue + ", City: " + cityName);
        }
    }
}