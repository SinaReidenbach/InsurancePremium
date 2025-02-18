package com.sina_reidenbach.versicherungspraemienrechner.repositoryTests;
import com.sina_reidenbach.InsurancePremium.model.Region;
import com.sina_reidenbach.InsurancePremium.model.Vehicle;
import com.sina_reidenbach.InsurancePremium.repository.RegionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class RegionRepositoryTest {

    @Mock
    private RegionRepository regionRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Region region = new Region("Nordrhein-Westfalen",1.5);
        when(regionRepository.findByName("Nordrhein-Westfalen")).thenReturn(Optional.of(region));
        when(regionRepository.findByCities_Name("Leverkusen")).thenReturn(region);
        when(regionRepository.findByPostcodeValueStartingWith("51373")).thenReturn(Optional.of(region));

    }


    @Test
    void testFindByRegionName() {

        Optional<Region> region = regionRepository.findByName("Nordrhein-Westfalen");
        assertTrue(region.isPresent());

        Region result = region.get();


        assertEquals("Nordrhein-Westfalen", result.getName().strip());
        assertEquals(1.5, result.getFactor());
    }

    @Test
    void testfindByCities_Name() {

        Region result = regionRepository.findByCities_Name("Leverkusen");

        assertEquals("Nordrhein-Westfalen", result.getName());
        assertEquals(1.5, result.getFactor());
    }

    @Test
    void testfindByPostcodeValueStartingWith() {

        Optional<Region> region = regionRepository.findByPostcodeValueStartingWith("51373");
        assertTrue(region.isPresent());

        Region result = region.get();

        assertEquals("Nordrhein-Westfalen", result.getName());
        assertEquals(1.5, result.getFactor());


    }
}
