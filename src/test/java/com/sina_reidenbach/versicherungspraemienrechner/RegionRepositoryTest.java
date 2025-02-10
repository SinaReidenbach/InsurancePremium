package com.sina_reidenbach.versicherungspraemienrechner;
import com.sina_reidenbach.InsurancePremium.model.Region;
import com.sina_reidenbach.InsurancePremium.repository.RegionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class RegionRepositoryTest {

    @Mock
    private RegionRepository regionRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    Region region = new Region("79189", "Baden-Württemberg");

    @Test
    void testFindByRegionName() {
        assertNotNull(region);
        assertEquals("Baden-Württemberg", region.getRegionName());
    }
}