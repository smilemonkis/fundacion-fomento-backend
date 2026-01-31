package com.fundacionfomentodb.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record CreateProyectoRequest(
        @NotBlank(message = "El código es requerido")
        String codigo,

        @NotBlank(message = "El nombre es requerido")
        String nombre,

        @NotBlank(message = "La descripción es requerida")
        String descripcion,

        @NotNull(message = "La fecha de inicio es requerida")
        LocalDate fechaInicio,

        @NotNull(message = "La fecha de fin es requerida")
        @Future(message = "La fecha de fin debe ser futura")
        LocalDate fechaFin
) {
}
