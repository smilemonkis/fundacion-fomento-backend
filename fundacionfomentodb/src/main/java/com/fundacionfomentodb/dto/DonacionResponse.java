package com.fundacionfomentodb.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DonacionResponse(
        Integer id,
        Integer usuarioId,
        String usuarioEmail,
        BigDecimal monto,
        String destino,
        Integer proyectoId,
        String proyectoNombre,
        String estado,
        LocalDateTime fecha
) {
}
