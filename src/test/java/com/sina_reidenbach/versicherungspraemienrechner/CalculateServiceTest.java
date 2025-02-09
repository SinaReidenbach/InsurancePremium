package com.sina_reidenbach.versicherungspraemienrechner;
import com.sina_reidenbach.InsurancePremium.service.CalculateService;
import com.sina_reidenbach.InsurancePremium.service.FactorService;
import com.sina_reidenbach.InsurancePremium.service.PostcodeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class CalculateServiceTest {

    @Mock
    private FactorService factorService;
    @Mock
    private PostcodeService postcodeService;

    private CalculateService calculateService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        calculateService = new CalculateService(factorService, null, postcodeService);
        when(factorService.calculatePremium(10000, "SUV", "Baden-Württemberg"))
                .thenReturn(1.5);
    }

    @Test
    void testCalculatePremium() {
        double premium = factorService.calculatePremium(10000, "SUV", "Baden-Württemberg");
        assertEquals(1.5, premium);
    }
}