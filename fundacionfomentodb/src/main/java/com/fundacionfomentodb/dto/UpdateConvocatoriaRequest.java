package com.fundacionfomentodb.dto;

import java.time.LocalDate;

public record UpdateConvocatoriaRequest(
        String titulo,
        String descripcion,
        LocalDate fechaInicio,
        LocalDate fechaFin
) {
}
