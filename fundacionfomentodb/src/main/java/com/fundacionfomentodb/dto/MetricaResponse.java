package com.fundacionfomentodb.dto;

public record MetricaResponse(
        Integer id,
        String  label,
        String  valor,
        String  icono,
        Integer orden,
        Boolean activo
) {}