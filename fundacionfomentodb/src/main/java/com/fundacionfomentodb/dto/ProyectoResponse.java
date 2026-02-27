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
        String imagenUrl,
        Integer beneficiarios,
        String presupuesto,
        Integer progreso,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}