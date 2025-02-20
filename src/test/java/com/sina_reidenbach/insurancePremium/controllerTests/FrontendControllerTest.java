package com.sina_reidenbach.insurancePremium.controllerTests;

import com.sina_reidenbach.InsurancePremium.controller.FrontendController;
import com.sina_reidenbach.InsurancePremium.model.City;
import com.sina_reidenbach.InsurancePremium.model.Postcode;
import com.sina_reidenbach.InsurancePremium.model.Region;
import com.sina_reidenbach.InsurancePremium.model.Vehicle;
import com.sina_reidenbach.InsurancePremium.repository.*;
import com.sina_reidenbach.InsurancePremium.service.CalculateService;
import com.sina_reidenbach.InsurancePremium.service.StatisticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Optional;

public class FrontendControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CalculateService calculateService;

    @Mock
    private RegionRepository regionRepository;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private PostcodeRepository postcodeRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private StatisticsService statisticsService;

    @InjectMocks
    private FrontendController frontendController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(frontendController).build();
    }

    @Test
    void testBerechnen_withValidInput_returnsPremiumAndModelAttributes() throws Exception {
        // Speichern des aktuellen Default-Loales
        Locale defaultLocale = Locale.getDefault();
        try {
            // Setze Locale auf Englisch (USA), um das Dezimaltrennzeichen auf Punkt zu setzen
            Locale.setDefault(Locale.US);

            City city = new City();
            city.setName("Stuttgart");
            when(cityRepository.findByPostcodes_PostcodeValue("70173")).thenReturn(city);

            Region region = new Region();
            region.setName("Baden-Württemberg");
            when(regionRepository.findByCities_Name("Stuttgart")).thenReturn(region);

            Vehicle vehicle = new Vehicle();
            vehicle.setId(1L);
            vehicle.setName("SUV");
            when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));

            when(calculateService.calculatePremium(10000, 10000, 1L, "70173")).thenReturn(225.0);
            when(vehicleRepository.findAll()).thenReturn(Collections.singletonList(vehicle));

            mockMvc.perform(post("/berechnen")
                            .param("km", "10000")
                            .param("postcodeValue", "70173")
                            .param("vehicle", "1")
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("premium"))
                    .andExpect(model().attributeExists("region"))
                    .andExpect(model().attributeExists("vehicleList"))
                    .andExpect(model().attribute("premium", "225.00 €"))
                    .andExpect(model().attribute("region", "Baden-Württemberg"))
                    .andExpect(view().name("index"));
        } finally {
            // Stelle das ursprüngliche Locale wieder her
            Locale.setDefault(defaultLocale);
        }
    }

    @Test
    void testBerechnen_withMissingParameters_returnsErrorPage() throws Exception {
        mockMvc.perform(post("/berechnen")
                        .param("km", "")
                        .param("postcodeValue", "")
                        .param("vehicle", "")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testFilterPostcodes_withMatchingInput_returnsFilteredPostcodes() throws Exception {
        Postcode postcode1 = new Postcode();
        postcode1.setPostcodeValue("70173");
        Postcode postcode2 = new Postcode();
        postcode2.setPostcodeValue("70174");

        when(postcodeRepository.findAll()).thenReturn(Arrays.asList(postcode1, postcode2));

        mockMvc.perform(get("/filter-postcodes")
                        .param("input", "7017"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<option value=\"70173\">70173</option>")))
                .andExpect(content().string(containsString("<option value=\"70174\">70174</option>")));
    }

    @Test
    void testFilterPostcodes_withNoMatchingInput_returnsEmptyOptions() throws Exception {
        when(postcodeRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/filter-postcodes")
                        .param("input", "9999"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void testShowHomePage_withNoInput_displaysIndexWithLists() throws Exception {
        Postcode postcode = new Postcode();
        postcode.setPostcodeValue("70173");

        Vehicle vehicle = new Vehicle();
        vehicle.setName("SUV");

        when(postcodeRepository.findAll()).thenReturn(Collections.singletonList(postcode));
        when(vehicleRepository.findAll()).thenReturn(Collections.singletonList(vehicle));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("postcodeList"))
                .andExpect(model().attributeExists("vehicleList"))
                .andExpect(view().name("index"));
    }

    @Test
    void testShowHomePage_withEmptyRepositories_displaysEmptyLists() throws Exception {
        when(postcodeRepository.findAll()).thenReturn(Collections.emptyList());
        when(vehicleRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("postcodeList", Collections.emptyList()))
                .andExpect(model().attribute("vehicleList", Collections.emptyList()))
                .andExpect(view().name("index"));
    }
}
