package com.estacionamento.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketRequestDTO {

    @NotBlank(message = "A placa do veículo é obrigatória")
    private String licensePlate;

    @NotNull(message = "O ID da zona de estacionamento é obrigatório")
    private Long zoneId;

    @NotNull(message = "A duração em minutos é obrigatória")
    @Min(value = 15, message = "O tempo mínimo de permanência é de 15 minutos")
    private Integer durationMinutes;
}