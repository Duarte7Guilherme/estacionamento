package com.estacionamento.repository;

import com.estacionamento.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Optional<Vehicle> findByLicensePlate(String licensePlate);
    List<Vehicle> findByUserId(Long userId);
    boolean existsByLicensePlate(String licensePlate);
}
