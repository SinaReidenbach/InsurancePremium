package com.sina_reidenbach.InsurancePremium.repository;

import com.sina_reidenbach.InsurancePremium.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface CityRepository extends JpaRepository<City, Long> {
    Optional<City> findFirstByName(String name);

    City findByPostcodes_PostcodeValue(String postcodeValue);
};

