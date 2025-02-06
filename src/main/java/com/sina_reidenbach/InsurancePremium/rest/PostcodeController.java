package com.sina_reidenbach.InsurancePremium.rest;

import com.sina_reidenbach.InsurancePremium.service.PostcodeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/postcode")
public class PostcodeController {
    private final PostcodeService postcodeService;

    public PostcodeController(PostcodeService postcodeService) {
        this.postcodeService = postcodeService;
    }

    @GetMapping("/{postcode}")
    public String getRegion(@PathVariable String postcode) {
        return postcodeService.getRegionByPostcode(postcode);
    }
}