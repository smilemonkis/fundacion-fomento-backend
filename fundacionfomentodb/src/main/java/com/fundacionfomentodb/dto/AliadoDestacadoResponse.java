package com.fundacionfomentodb.dto;

public record AliadoDestacadoResponse(
        Integer id,
        String  nombre,
        String  logoUrl,
        String  sitioWeb,
        Integer orden,
        Boolean activo
) {}