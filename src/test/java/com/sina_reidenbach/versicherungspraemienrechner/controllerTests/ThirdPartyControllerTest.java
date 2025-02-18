package com.sina_reidenbach.versicherungspraemienrechner.controllerTests;

import com.sina_reidenbach.InsurancePremium.dto.AnnoKilometersResponse;
import com.sina_reidenbach.InsurancePremium.dto.PremiumResponse;
import com.sina_reidenbach.InsurancePremium.dto.RegionResponse;
import com.sina_reidenbach.InsurancePremium.dto.VehicleResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

public class ThirdPartyControllerTest {
    @GetMapping("/api/options/vehicles")
    void testGetVehicles() {
    }

    @GetMapping("/api/options/regions")
    void testGetRegions() {
    }

    @GetMapping("/api/options/annoKilometers")
    void testGetAnnoKilometers() {
    }

    @PostMapping("/api/calculate")
    void testCalculatePremium() {
    }
}

