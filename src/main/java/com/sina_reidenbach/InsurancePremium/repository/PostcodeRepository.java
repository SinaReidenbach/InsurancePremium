package com.sina_reidenbach.InsurancePremium.repository;
import com.sina_reidenbach.InsurancePremium.model.Postcode;
import com.sina_reidenbach.InsurancePremium.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostcodeRepository extends JpaRepository<Postcode, Long> {
    Postcode findByPostcodeValue(String postcodeValue);
}
