package com.sina_reidenbach.InsurancePremium.repository;

import com.sina_reidenbach.InsurancePremium.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {
    Vehicle findByVehicleName(String vehicleName);
}