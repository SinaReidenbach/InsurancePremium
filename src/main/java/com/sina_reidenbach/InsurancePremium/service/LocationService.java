package com.sina_reidenbach.InsurancePremium.service;

import com.sina_reidenbach.InsurancePremium.model.City;
import com.sina_reidenbach.InsurancePremium.model.Postcode;
import com.sina_reidenbach.InsurancePremium.model.Region;
import com.sina_reidenbach.InsurancePremium.repository.CityRepository;
import com.sina_reidenbach.InsurancePremium.repository.PostcodeRepository;
import com.sina_reidenbach.InsurancePremium.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

    @Autowired
    private PostcodeRepository postcodeRepository;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private RegionRepository regionRepository;

    public Optional<Postcode> findPostcode(String postcodeValue) {
        return postcodeRepository.findFirstByPostcodeValue(postcodeValue);
    }

    public Optional<City> findCityByPostcode(String postcodeValue) {
        return Optional.ofNullable(cityRepository.findByPostcodes_PostcodeValue(postcodeValue));
    }

    public Optional<Region> findRegionByCityName(String cityName) {
        return Optional.ofNullable(regionRepository.findByCities_Name(cityName));
    }

    public List<Postcode> filterPostcodesByInput(String input) {
        return postcodeRepository.findAll().stream()
                .filter(postcode -> postcode.getPostcodeValue().startsWith(input))
                .toList();
    }
}