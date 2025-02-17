package com.sina_reidenbach.InsurancePremium.repository;

import com.sina_reidenbach.InsurancePremium.model.City;
import com.sina_reidenbach.InsurancePremium.model.Postcode;
import com.sina_reidenbach.InsurancePremium.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface CityRepository extends JpaRepository<City, Long> {
    Optional<City> findFirstByName(String name);

    City findByPostcodes(List<Postcode> postcodes);
    City findByPostcodes_PostcodeValue(String postcodeValue);
};

