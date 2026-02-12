package com.fundacionfomentodb.dto;

import jakarta.validation.constraints.NotNull;

public record CreateInscripcionRequest(
        @NotNull(message = "El ID del usuario es requerido")
        Integer usuarioId,

        @NotNull(message = "El ID de la convocatoria es requerido")
        Integer convocatoriaId,

        String observaciones
) {
}
