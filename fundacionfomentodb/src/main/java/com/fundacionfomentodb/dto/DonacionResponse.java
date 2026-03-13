package com.fundacionfomentodb.dto;

import java.math.BigDecimal;

public record DonacionResponse(
        Integer    id,
        Integer    usuarioId,
        String     usuarioEmail,
        String     donanteNombre,
        String     donanteEmail,
        BigDecimal monto,
        String     destino,
        Integer    proyectoId,
        String     proyectoNombre,
        String     estado,
        String     fecha
) {}
