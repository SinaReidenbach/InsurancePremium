package com.sina_reidenbach.InsurancePremium.repository;

import com.sina_reidenbach.InsurancePremium.model.Anno_Kilometers;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AnnoKilometersRepository extends JpaRepository<Anno_Kilometers, Long> {

    List<Anno_Kilometers> findByMinLessThanEqualAndMaxGreaterThanEqual(int min, int max);
    Optional<Anno_Kilometers> findById(Long id);
}