package com.estacionamento.repository;

import com.estacionamento.model.ParkingZone;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ParkingZoneRepository extends JpaRepository<ParkingZone, Long> {
    List<ParkingZone> findByActiveTrue();
}