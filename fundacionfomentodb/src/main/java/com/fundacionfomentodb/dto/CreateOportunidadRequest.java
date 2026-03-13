package com.fundacionfomentodb.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record CreateOportunidadRequest(
        @NotBlank(message = "El título es requerido")
        String titulo,

        @NotBlank(message = "La descripción es requerida")
        String descripcion,

        String imagenUrl,

        @NotNull(message = "El tipo es requerido")
        String tipo,

        LocalDate fechaLimite,
        String enlace
) {}