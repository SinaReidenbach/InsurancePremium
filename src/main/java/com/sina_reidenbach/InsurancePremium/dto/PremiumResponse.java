package com.sina_reidenbach.InsurancePremium.dto;

import java.util.Map;

public class PremiumResponse {
    private Map<String, Object> premium;

    // Constructor
    public PremiumResponse() {}

    public PremiumResponse(Map<String, Object> premium) {
        this.premium = premium;
    }

    public Map<String, Object> getPremium() {
        return premium;
    }

    public void setPremium(Map<String, Object> premium) {
        this.premium = premium;
    }
}
