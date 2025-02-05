package com.sina_reidenbach.InsurancePremium.repository;

import com.sina_reidenbach.InsurancePremium.model.AnnoKilometers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AnnoKilometersRepository extends JpaRepository<AnnoKilometers, Integer> {

    // Benutzerdefinierte Methode, um Kilometerwerte im Bereich von km_min und km_max zu finden
    List<AnnoKilometers> findByKmMinLessThanEqualAndKmMaxGreaterThanEqual(int kmMin, int kmMax);
}