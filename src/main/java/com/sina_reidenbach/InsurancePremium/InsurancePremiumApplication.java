package com.sina_reidenbach.InsurancePremium;

import com.sina_reidenbach.InsurancePremium.service.CalculateService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InsurancePremiumApplication {

	@Autowired
	private CalculateService insurancePremiumCalculate;

	public static void main(String[] args) {
		SpringApplication.run(InsurancePremiumApplication.class, args);
	}

	// Diese Methode wird nach dem Starten der Spring Boot-Anwendung ausgeführt
	@PostConstruct
	public void runTestAfterStartup() {
		insurancePremiumCalculate.test();
	}
}