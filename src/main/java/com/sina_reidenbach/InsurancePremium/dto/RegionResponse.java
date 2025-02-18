package com.sina_reidenbach.InsurancePremium.dto;

import java.util.Map;

public class RegionResponse {
    private Map<Long, Map<String, Object>> regions;

    public Map<Long, Map<String, Object>> getRegions() {
        return regions;
    }

    public void setRegions(Map<Long, Map<String, Object>> regions) {
        this.regions = regions;
    }
}