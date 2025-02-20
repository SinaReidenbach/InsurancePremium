package com.sina_reidenbach.insurancePremium.service;

import com.sina_reidenbach.InsurancePremium.model.Anno_Kilometers;
import com.sina_reidenbach.InsurancePremium.repository.AnnoKilometersRepository;
import com.sina_reidenbach.InsurancePremium.repository.RegionRepository;
import com.sina_reidenbach.InsurancePremium.repository.VehicleRepository;
import com.sina_reidenbach.InsurancePremium.service.CalculateService;
import com.sina_reidenbach.InsurancePremium.model.Region;
import com.sina_reidenbach.InsurancePremium.model.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Collections;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CalculateServiceTest {

    @Mock
    private RegionRepository regionRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private AnnoKilometersRepository annoKilometersRepository;

    @InjectMocks
    private CalculateService calculateService; // Mockito injiziert die Mocks

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialisiert alle @Mock-Annotationen

        Region region = new Region();
        region.setFactor(1.5);

        Vehicle vehicle = new Vehicle();
        vehicle.setFactor(1.5);

        Anno_Kilometers annoKilometers = new Anno_Kilometers();
        annoKilometers.setFactor(1.0);


        when(regionRepository.findByPostcodeValueStartingWith("51373"))
                .thenReturn(Optional.of(region));

        when(vehicleRepository.findById(14L))
                .thenReturn(Optional.of(vehicle));

        when(annoKilometersRepository.findByMinLessThanEqualAndMaxGreaterThanEqual(2000, 2000))
                .thenReturn(Collections.singletonList(annoKilometers));

    }

    @Test
    void testCalculateRegionFactor() {
        double regionFactor = calculateService.calculateRegionFactor("51373");
        assertEquals(1.5, regionFactor);
    }
    @Test
    void testCalculateVehicleFactor() {
        double vehicleFactor = calculateService.calculateVehicleFactor(14L);
        assertEquals(1.5, vehicleFactor);
    }

    @Test
    void testCalculateAnnoKilometersFactor() {
        when(annoKilometersRepository.findByMinLessThanEqualAndMaxGreaterThanEqual(2000,2000))
                .thenReturn(Collections.singletonList(new Anno_Kilometers(2000,2000,1.0)));

        double kmFactor = calculateService.calculateAnnoKilometersFactor(2000, 2000);

        assertEquals(1.0, kmFactor);

    }


}
