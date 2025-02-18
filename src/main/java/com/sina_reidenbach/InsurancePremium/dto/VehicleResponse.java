package com.sina_reidenbach.InsurancePremium.dto;

import java.util.Map;

public class VehicleResponse {
    private Map<Long, Map<String, Object>> vehicles;

    public Map<Long, Map<String, Object>> getVehicles() {
        return vehicles;
    }

    public void setVehicles(Map<Long, Map<String, Object>> vehicles) {
        this.vehicles = vehicles;
    }
}
