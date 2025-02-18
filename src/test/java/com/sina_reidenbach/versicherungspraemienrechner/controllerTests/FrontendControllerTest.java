package com.sina_reidenbach.versicherungspraemienrechner.controllerTests;

import com.sina_reidenbach.InsurancePremium.model.City;
import com.sina_reidenbach.InsurancePremium.model.Postcode;
import com.sina_reidenbach.InsurancePremium.model.Region;
import com.sina_reidenbach.InsurancePremium.model.Vehicle;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FrontendControllerTest {
    @PostMapping("/berechnen")
    void testBerechnen() {
    }


    @GetMapping("/filter-postcodes")
    @ResponseBody
    void testFilterPostcodes() {
    }

    @GetMapping("/")
    @Transactional
    void testShowHomePage() {
    }
}
