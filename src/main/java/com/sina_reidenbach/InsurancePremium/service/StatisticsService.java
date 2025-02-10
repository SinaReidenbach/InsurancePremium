package com.sina_reidenbach.InsurancePremium.service;

import com.sina_reidenbach.InsurancePremium.model.Statistics;
import com.sina_reidenbach.InsurancePremium.repository.StatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class StatisticsService {
    @Autowired
    private StatisticsRepository statisticsRepository;

    public void saveStatistics(LocalDateTime dateTime, String postcode, String vehicleName, int km,double premium) {
        Statistics entity = new Statistics(dateTime, postcode, vehicleName, km,premium);
        statisticsRepository.save(entity);
    }

}