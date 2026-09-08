package com.estacionamento.controller;

import com.estacionamento.dto.TicketRequestDTO;
import com.estacionamento.dto.TicketResponseDTO;
import com.estacionamento.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<TicketResponseDTO> createTicket(@Valid @RequestBody TicketRequestDTO dto) {
        TicketResponseDTO response = ticketService.createTicket(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
