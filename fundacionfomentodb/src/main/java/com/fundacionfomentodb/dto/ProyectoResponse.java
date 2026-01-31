package com.fundacionfomentodb.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ProyectoResponse(
        Integer id,
        String codigo,
        String nombre,
        String descripcion,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        Boolean activo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
