package com.fundacionfomentodb.dto;

public record UpdateAliadoDestacadoRequest(
        String  nombre,
        String  logoUrl,
        String  sitioWeb,
        Integer orden,
        Boolean activo
) {}