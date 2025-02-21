package com.sina_reidenbach.InsurancePremium.repository;

import com.sina_reidenbach.InsurancePremium.model.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface StatisticsRepository extends JpaRepository<Statistics, Long> {
    Optional<Statistics> findByDateTimeAndPostcodeAndVehicle(LocalDateTime dateTime, String postcode, String vehicleName);
}