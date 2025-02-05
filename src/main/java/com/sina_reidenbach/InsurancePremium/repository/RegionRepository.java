package com.sina_reidenbach.InsurancePremium.repository;

import com.sina_reidenbach.InsurancePremium.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, String> {
    Region findByRegionName(String regionName);
}