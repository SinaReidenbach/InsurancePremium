package com.sina_reidenbach.InsurancePremium.repository;
import com.sina_reidenbach.InsurancePremium.model.Postcode;
import com.sina_reidenbach.InsurancePremium.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostcodeRepository extends JpaRepository<Postcode, Long> {
    Optional<Postcode> findFirstByPostcodeValue(String value);
    List<Postcode> findByPostcodeValueStartingWith(String postcodeValue);
}
