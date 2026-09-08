package com.estacionamento.dto;

import com.estacionamento.model.Ticket;
import com.estacionamento.model.TicketStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class TicketResponseDTO {

    private Long id;
    private String licensePlate;
    private String zoneName;
    private LocalDateTime entryTime;
    private LocalDateTime expectedExitTime;
    private TicketStatus status;
    private BigDecimal totalAmount;

    public static TicketResponseDTO fromEntity(Ticket ticket) {
        return TicketResponseDTO.builder()
                .id(ticket.getId())
                .licensePlate(ticket.getVehicle().getLicensePlate())
                .zoneName(ticket.getZone().getName())
                .entryTime(ticket.getEntryTime())
                .expectedExitTime(ticket.getExpectedExitTime())
                .status(ticket.getStatus())
                .totalAmount(ticket.getTotalAmount())
                .build();
    }
}