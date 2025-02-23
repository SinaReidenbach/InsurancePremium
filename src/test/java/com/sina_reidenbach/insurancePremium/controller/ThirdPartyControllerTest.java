package com.sina_reidenbach.insurancePremium.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sina_reidenbach.InsurancePremium.controller.ThirdPartyController;
import com.sina_reidenbach.InsurancePremium.model.*;
import com.sina_reidenbach.InsurancePremium.repository.*;
import com.sina_reidenbach.InsurancePremium.service.CalculateService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ThirdPartyController.class)
public class ThirdPartyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CalculateService calculateService;

    @MockitoBean
    private VehicleRepository vehicleRepository;

    @MockitoBean
    private RegionRepository regionRepository;

    @MockitoBean
    private AnnoKilometersRepository annoKilometersRepository;

    @MockitoBean
    private PostcodeRepository postcodeRepository;

    @InjectMocks
    private ThirdPartyController thirdPartyController;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetVehicles() throws Exception {
        List<Vehicle> vehicles = Arrays.asList(
                new Vehicle(1L, "SUV", 1.5),
                new Vehicle(2L, "Sedan", 1.2)
        );
        when(vehicleRepository.findAll()).thenReturn(vehicles);

        mockMvc.perform(get("/api/options/vehicles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vehicles").exists())
                .andExpect(jsonPath("$.vehicles['1'].vehicleName").value("SUV"))
                .andExpect(jsonPath("$.vehicles['1'].factor").value(1.5))
                .andExpect(jsonPath("$.vehicles['2'].vehicleName").value("Sedan"))
                .andExpect(jsonPath("$.vehicles['2'].factor").value(1.2));
    }

    @Test
    void testGetRegions() throws Exception {
        List<Region> regions = new ArrayList<>();
        regions.add(new Region(1L, "Baden-Württemberg", 1.5));
        regions.add(new Region(2L, "Bavaria", 1.2));

        when(regionRepository.findAll()).thenReturn(regions);

        mockMvc.perform(get("/api/options/regions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.regions['1'].regionName").value("Baden-Württemberg"))
                .andExpect(jsonPath("$.regions['1'].factor").value(1.5))
                .andExpect(jsonPath("$.regions['2'].regionName").value("Bavaria"))
                .andExpect(jsonPath("$.regions['2'].factor").value(1.2));
    }


    @Test
    void testGetAnnoKilometers() throws Exception {
        List<Anno_Kilometers> annoKilometers = Arrays.asList(
                new Anno_Kilometers(0, 1000, 0.5),
                new Anno_Kilometers(1001, 10000, 1.0)
        );
        when(annoKilometersRepository.findAll()).thenReturn(annoKilometers);

        mockMvc.perform(get("/api/options/annoKilometers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.annoKilometers").exists())
                .andExpect(jsonPath("$.annoKilometers[0].min").value(0))
                .andExpect(jsonPath("$.annoKilometers[0].max").value(1000))
                .andExpect(jsonPath("$.annoKilometers[0].factor").value(0.5))
                .andExpect(jsonPath("$.annoKilometers[1].min").value(1001))
                .andExpect(jsonPath("$.annoKilometers[1].max").value(10000))
                .andExpect(jsonPath("$.annoKilometers[1].factor").value(1.0));
    }


    @Test
    void testCalculatePremium() throws Exception {
        Map<String, Object> premiumRequest = new HashMap<>();
        premiumRequest.put("vehicleId", 1L);
        premiumRequest.put("annoKilometers", 10000);
        premiumRequest.put("postcode", "70173");

        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(new Vehicle()));
        when(postcodeRepository.findFirstByPostcodeValue("70173")).thenReturn(Optional.of(new Postcode()));
        when(calculateService.calculatePremium(10000, 10000, 1L, "70173")).thenReturn(225.0);

        mockMvc.perform(post("/api/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(premiumRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.premium.premium").value(225.0));
    }
}