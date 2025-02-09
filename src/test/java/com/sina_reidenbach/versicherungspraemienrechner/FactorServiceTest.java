package com.sina_reidenbach.versicherungspraemienrechner;

import com.sina_reidenbach.InsurancePremium.model.AnnoKilometers;
import com.sina_reidenbach.InsurancePremium.model.Region;
import com.sina_reidenbach.InsurancePremium.model.Vehicle;
import com.sina_reidenbach.InsurancePremium.repository.AnnoKilometersRepository;
import com.sina_reidenbach.InsurancePremium.repository.RegionRepository;
import com.sina_reidenbach.InsurancePremium.repository.VehicleRepository;
import com.sina_reidenbach.InsurancePremium.service.FactorService;
import com.sina_reidenbach.InsurancePremium.service.PostcodeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FactorServiceTest {

    @Mock
    private AnnoKilometersRepository annoKilometersRepository;
    @Mock
    private VehicleRepository vehicleRepository;
    @Mock
    private RegionRepository regionRepository;
    @Mock
    private PostcodeService postcodeService;

    @InjectMocks
    private FactorService factorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialisiert die Mocks

        Region region = mock(Region.class);
        region.setRegionFactor(1.5);

        Vehicle vehicle = mock(Vehicle.class);
        vehicle.setVehicleName("SUV");
        vehicle.setVehicleFactor(1.5);

        when(annoKilometersRepository.findByKmMinLessThanEqualAndKmMaxGreaterThanEqual(2000, 2000))
                .thenReturn(List.of(new AnnoKilometers(0, 5000, 0.5)));
        when(region.getRegionFactor()).thenReturn(1.5);
        when(vehicleRepository.findByVehicleName("SUV")).thenReturn(vehicle);
        when(regionRepository.findByRegionName("Baden-Württemberg")).thenReturn(region);
        when(postcodeService.getRegionByPostcode("79189")).thenReturn("Baden-Württemberg");
        when(vehicle.getVehicleFactor()).thenReturn(1.5);
    }

    @Test
    void testGetKilometerFactor() {
        // Testen der verschiedenen Kilometerbereiche
        testKilometerFactor(0, 5000, 0.5);
        testKilometerFactor(5001, 10000, 1.0);
        testKilometerFactor(10001, 20000, 1.5);
        testKilometerFactor(20001, 20002, 2.0);
    }

    private void testKilometerFactor(int minKm, int maxKm, double expectedFactor) {
        for (int i = minKm; i <= maxKm; i += 5000) {
            when(annoKilometersRepository.findByKmMinLessThanEqualAndKmMaxGreaterThanEqual(i, i))
                    .thenReturn(List.of(new AnnoKilometers(i, i, expectedFactor)));
            double result = factorService.getKilometerFactor(i);
            assertEquals(expectedFactor, result, 0.001);
        }
    }

    @Test
    void testGetVehicleFactor() {

        double factor = factorService.getVehicleFactor("SUV");
        assertEquals(1.5, factor);
    }



    @Test
    void testGetRegionFactor() {

        double factor = factorService.getRegionFactor("Baden-Württemberg");
        assertEquals(1.5, factor);
    }

    @Test
    void testGetRegionFactorByPostcode() {

        double result = factorService.getRegionFactorByPostcode("79189");
        assertEquals(1.5, result); // Annahme: getRegionFactor() liefert den Faktor
    }

    @Test
    void testCalculatePremium() {

        double expectedFactor = 0.5*1.5*1.5;

        double result = factorService.calculatePremium(2000, "SUV","Baden-Württemberg");
        assertEquals(expectedFactor,result);
    }

}
