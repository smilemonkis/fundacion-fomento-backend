package com.fundacionfomentodb.dto;

public record SuscripcionResponse(
        Integer id,
        String email,
        Boolean activo,
        String fechaSuscripcion
) {}