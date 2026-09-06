package com.estacionamento.repository;

import com.estacionamento.model.Ticket;
import com.estacionamento.model.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByVehicleIdAndStatus(Long vehicleId, TicketStatus status);
    Optional<Ticket> findFirstByVehicleLicensePlateAndStatus(String licensePlate, TicketStatus status);
}
