package com.fundacionfomentodb.dto;

import jakarta.validation.constraints.NotBlank;

public record MetricaRequest(
        @NotBlank String label,
        @NotBlank String valor,
        String  icono,
        Integer orden,
        Boolean activo
) {}