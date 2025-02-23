package com.sina_reidenbach.insurancePremium.controller;

import com.sina_reidenbach.InsurancePremium.controller.FrontendController;
import com.sina_reidenbach.InsurancePremium.model.City;
import com.sina_reidenbach.InsurancePremium.model.Postcode;
import com.sina_reidenbach.InsurancePremium.model.Region;
import com.sina_reidenbach.InsurancePremium.model.Vehicle;
import com.sina_reidenbach.InsurancePremium.repository.CityRepository;
import com.sina_reidenbach.InsurancePremium.repository.PostcodeRepository;
import com.sina_reidenbach.InsurancePremium.repository.RegionRepository;
import com.sina_reidenbach.InsurancePremium.repository.VehicleRepository;
import com.sina_reidenbach.InsurancePremium.service.CalculateService;
import com.sina_reidenbach.InsurancePremium.service.LocationService;
import com.sina_reidenbach.InsurancePremium.service.StatisticsService;
import com.sina_reidenbach.InsurancePremium.service.VehicleService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.List;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    @Mock
    private LocationService locationService;

    @Mock
    private VehicleService vehicleService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private Model model;

    @Mock
    private Vehicle vehicle;

    @Mock
    private City city;

    @Mock
    private Region region;

    @Mock
    private Postcode postcode;

    @InjectMocks
    private FrontendController frontendController;

    private final String validPostcode = "12345";
    private final Long vehicleId = 1L;
    private final int km = 1000;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(frontendController, "locationService", locationService);
        ReflectionTestUtils.setField(frontendController, "vehicleService", vehicleService);
        mockMvc = MockMvcBuilders.standaloneSetup(frontendController).build();
    }

    @Test
    void testBerechnen_WithValidData() {
        when(locationService.findPostcode(validPostcode)).thenReturn(Optional.of(postcode));
        when(cityRepository.findByPostcodes_PostcodeValue(validPostcode)).thenReturn(city);
        when(locationService.findCityByPostcode(validPostcode)).thenReturn(Optional.of(new City("DummyCity")));
        when(regionRepository.findByCities_Name(city.getName())).thenReturn(region);
        when(locationService.findRegionByCityName("DummyCity")).thenReturn(Optional.of(new Region("Baden-Württemberg", 1.0)));
        when(region.getName()).thenReturn("Baden-Württemberg");
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle));
        when(vehicle.getName()).thenReturn("SUV");
        when(calculateService.calculatePremium(km, km, vehicleId, validPostcode)).thenReturn(500.0);
        when(vehicleService.getSortedVehicleList()).thenReturn(new ArrayList<>(List.of(vehicle)));
        when(locationService.filterPostcodesByInput(anyString())).thenReturn(Collections.emptyList());

        String result = frontendController.berechnen(km, validPostcode, vehicleId, request, model);

        assertEquals("index", result);
        verify(model).addAttribute("premium", "500,00 €");
        verify(model).addAttribute("region", "Baden-Württemberg");
        verify(model).addAttribute("vehicleList", List.of(vehicle));
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

        when(locationService.filterPostcodesByInput("7017")).thenReturn(Arrays.asList(postcode1, postcode2));

        mockMvc.perform(get("/filter-postcodes")
                        .param("input", "7017"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<option value=\"70173\">70173</option>")))
                .andExpect(content().string(containsString("<option value=\"70174\">70174</option>")));
    }

    @Test
    void testFilterPostcodes_withNoMatchingInput_returnsEmptyOptions() throws Exception {
        when(locationService.filterPostcodesByInput("9999")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/filter-postcodes")
                        .param("input", "9999"))
                .andExpect(status().isOk())
                .andExpect(content().string("<option value=\"\">Keine Postleitzahlen gefunden</option>"));
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
