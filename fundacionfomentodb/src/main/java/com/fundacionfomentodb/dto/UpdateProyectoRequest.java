package com.fundacionfomentodb.dto;

import java.time.LocalDate;

public record UpdateProyectoRequest(
        String codigo,
        String nombre,
        String descripcion,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        String imagenUrl,
        Integer beneficiarios,
        String presupuesto,
        Integer progreso
) {
}