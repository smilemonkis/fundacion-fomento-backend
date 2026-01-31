package com.fundacionfomentodb.dto;

import java.time.LocalDateTime;

public record InscripcionResponse(
        Integer id,
        Integer usuarioId,
        String usuarioEmail,
        Integer convocatoriaId,
        String convocatoriaTitulo,
        String estado,
        String observaciones,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
