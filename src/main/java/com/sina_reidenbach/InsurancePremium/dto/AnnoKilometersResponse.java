package com.sina_reidenbach.InsurancePremium.dto;

import java.util.List;
import java.util.Map;

public class AnnoKilometersResponse {
    private List<Map<String, Object>> annoKilometers;

    public List<Map<String, Object>> getAnnoKilometers() {
        return annoKilometers;
    }

    public void setAnnoKilometers(List<Map<String, Object>> annoKilometers) {
        this.annoKilometers = annoKilometers;
    }
}

