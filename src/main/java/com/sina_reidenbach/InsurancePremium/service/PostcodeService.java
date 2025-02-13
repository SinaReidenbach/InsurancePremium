package com.sina_reidenbach.InsurancePremium.service;

import com.sina_reidenbach.InsurancePremium.model.Region;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostcodeService {
    private final List<Region> postcodes = new ArrayList<>();

    @PostConstruct
    public void loadCSVData() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new ClassPathResource("postcodes.csv").getInputStream()))) {

            postcodes.addAll(br.lines()
                    .skip(1) // Erste Zeile (Header) überspringen
                    .map(this::parseLine)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Region parseLine(String line) {
        String[] fields = line.split(",");
        if (fields.length < 7) {
            return null;
        }
        String postcodeValue = fields[6].replace("\"", "").trim();
        String regionName = fields[2].replace("\"", "").trim();

        return new Region();
    }

    public String getRegionByPostcode(String postcode) {
        return postcodes.stream()
                .filter(loc -> loc.getPostcodeValue().equals(postcode))
                .map(Region::getName)
                .findFirst()
                .orElse("Region nicht gefunden");
    }
}