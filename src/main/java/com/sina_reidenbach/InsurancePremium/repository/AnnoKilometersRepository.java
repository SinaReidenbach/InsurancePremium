package com.sina_reidenbach.InsurancePremium.repository;

import com.sina_reidenbach.InsurancePremium.model.Anno_Kilometers;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AnnoKilometersRepository extends JpaRepository<Anno_Kilometers, Long> {

    // Benutzerdefinierte Methode, um Kilometerwerte im Bereich von km_min und km_max zu finden
    List<Anno_Kilometers> findByMinLessThanEqualAndMaxGreaterThanEqual(int min, int max);
    Optional<Anno_Kilometers> findById(Long id);
    Optional<Anno_Kilometers> findByFactor(double factor);
}