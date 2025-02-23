package com.sina_reidenbach.InsurancePremium.repository;

import com.sina_reidenbach.InsurancePremium.model.City;
import com.sina_reidenbach.InsurancePremium.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RegionRepository extends JpaRepository<Region, Long> {
    Optional<Region> findByName(String name);
    Region findByCities_Name(String cityName);
    @Query("SELECT r FROM Region r " +
            "JOIN r.cities c " +
            "JOIN c.postcodes p " +
            "WHERE p.postcodeValue LIKE :postcodeValue%")
    Optional<Region> findByPostcodeValueStartingWith(@Param("postcodeValue") String postcodeValue);





}
