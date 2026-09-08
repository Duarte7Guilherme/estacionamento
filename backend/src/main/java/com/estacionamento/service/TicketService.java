package com.estacionamento.service;

import com.estacionamento.dto.TicketRequestDTO;
import com.estacionamento.dto.TicketResponseDTO;
import com.estacionamento.exception.BusinessException;
import com.estacionamento.exception.ResourceNotFoundException;
import com.estacionamento.model.ParkingZone;
import com.estacionamento.model.Ticket;
import com.estacionamento.model.TicketStatus;
import com.estacionamento.model.Vehicle;
import com.estacionamento.repository.ParkingZoneRepository;
import com.estacionamento.repository.TicketRepository;
import com.estacionamento.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final VehicleRepository vehicleRepository;
    private final ParkingZoneRepository zoneRepository;

    @Transactional
    public TicketResponseDTO createTicket(TicketRequestDTO dto) {
        // 1. Buscar e validar o veículo
        Vehicle vehicle = vehicleRepository.findByLicensePlate(dto.getLicensePlate())
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado com a placa: " + dto.getLicensePlate()));

        // 2. Garantir que não há ticket ACTIVE para este veículo
        ticketRepository.findFirstByVehicleLicensePlateAndStatus(dto.getLicensePlate(), TicketStatus.ACTIVE)
                .ifPresent(t -> {
                    throw new BusinessException("Este veículo já possui um ticket ativo em andamento.");
                });

        // 3. Buscar e validar a zona de estacionamento
        ParkingZone zone = zoneRepository.findById(dto.getZoneId())
                .orElseThrow(() -> new ResourceNotFoundException("Zona de estacionamento não encontrada."));

        if (!zone.getActive()) {
            throw new BusinessException("Esta zona de estacionamento está desativada.");
        }

        if (zone.getOccupiedSpots() >= zone.getTotalSpots()) {
            throw new BusinessException("Não há vagas disponíveis na zona: " + zone.getName());
        }

        // 4. Calcular tempo e valores
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expectedExit = now.plusMinutes(dto.getDurationMinutes());

        // Cálculo proporcional do valor: (taxa_por_hora / 60) * minutos
        BigDecimal ratePerMinute = zone.getHourlyRate().divide(BigDecimal.valueOf(60), 4, RoundingMode.HALF_UP);
        BigDecimal totalAmount = ratePerMinute.multiply(BigDecimal.valueOf(dto.getDurationMinutes()))
                .setScale(2, RoundingMode.HALF_UP);

        // 5. Atualizar vagas ocupadas
        zone.setOccupiedSpots(zone.getOccupiedSpots() + 1);
        zoneRepository.save(zone);

        // 6. Criar e persistir o ticket
        Ticket ticket = Ticket.builder()
                .vehicle(vehicle)
                .zone(zone)
                .entryTime(now)
                .expectedExitTime(expectedExit)
                .status(TicketStatus.ACTIVE)
                .totalAmount(totalAmount)
                .build();

        Ticket savedTicket = ticketRepository.save(ticket);

        return TicketResponseDTO.fromEntity(savedTicket);
    }
}
