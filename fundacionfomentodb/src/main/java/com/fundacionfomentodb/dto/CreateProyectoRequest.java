package com.fundacionfomentodb.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public record CreateProyectoRequest(
        @NotBlank String codigo,
        @NotBlank String nombre,
        @NotBlank String descripcion,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        String    imagenUrl,
        Integer   beneficiarios,
        String    presupuesto,
        Integer   progreso,
        String    estado
) {}