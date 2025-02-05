package com.sina_reidenbach.InsurancePremium.repository;

import com.sina_reidenbach.InsurancePremium.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, String> {
    Vehicle findByVehicleName(String vehicleName);
}