package com.sina_reidenbach.InsurancePremium.service;

import com.sina_reidenbach.InsurancePremium.model.Statistics;
import com.sina_reidenbach.InsurancePremium.repository.StatisticsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class StatisticsService {
    @Autowired
    private StatisticsRepository statisticsRepository;

    private static final Logger logger = LoggerFactory.getLogger(StatisticsService.class);

    public void saveStatistics(LocalDateTime dateTime, String postcode, String vehicleName, int km, double premium, String ipAddress) {
        // Überprüfen, ob der Eintrag bereits existiert
        Optional<Statistics> existingEntry = statisticsRepository.findByDateTimeAndPostcodeAndVehicle(dateTime, postcode, vehicleName);

        if (existingEntry.isPresent()) {
            logger.info("Duplicate entry found for vehicle: {}, postcode: {}, dateTime: {}", vehicleName, postcode, dateTime);
            return; // Verhindert das Speichern von Duplikaten
        }

        // Neuer Eintrag erstellen und speichern
        Statistics entity = new Statistics(dateTime, postcode, vehicleName, km, premium, ipAddress);
        statisticsRepository.save(entity);

        logger.info("Statistics entry saved for vehicle: {}, postcode: {}, dateTime: {}", vehicleName, postcode, dateTime);
    }
}