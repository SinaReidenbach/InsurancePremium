package com.sina_reidenbach.versicherungspraemienrechner;
import com.sina_reidenbach.InsurancePremium.model.Vehicle;
import com.sina_reidenbach.InsurancePremium.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class VehicleRepositoryTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Vehicle vehicle = new Vehicle("SUV",1.5);
        when(vehicleRepository.findByVehicleName("SUV")).thenReturn(vehicle);
    }

    @Test
    void testFindByVehicleName() {
        Vehicle result = vehicleRepository.findByVehicleName("SUV");
        assertNotNull(result);
        assertEquals("SUV", result.getVehicleName());
        assertEquals(1.5, result.getVehicleFactor());
    }
}