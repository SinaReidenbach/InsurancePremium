package com.sina_reidenbach.InsurancePremium.repository;

import com.sina_reidenbach.InsurancePremium.model.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatisticsRepository extends JpaRepository<Statistics, Long> {
    // Hier kannst du benutzerdefinierte Abfragen hinzufügen, falls nötig
}