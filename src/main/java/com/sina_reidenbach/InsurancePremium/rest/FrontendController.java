package com.sina_reidenbach.InsurancePremium.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontendController {

    @GetMapping("/")
    public String showHomePage() {
        return "redirect:/index.html";  // Lädt die Datei aus src/main/resources/static/
    }
}