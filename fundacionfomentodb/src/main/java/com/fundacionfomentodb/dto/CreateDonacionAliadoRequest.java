package com.fundacionfomentodb.dto;
import java.math.BigDecimal;

public record CreateDonacionAliadoRequest(
        Integer usuarioId,
        BigDecimal monto,
        String destino,
        Integer proyectoId
) {}