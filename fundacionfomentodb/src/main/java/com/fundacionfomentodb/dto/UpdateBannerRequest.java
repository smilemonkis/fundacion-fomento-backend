package com.fundacionfomentodb.dto;

public record UpdateBannerRequest(
        String titulo,
        String subtitulo,
        String imagenUrl,
        String ctaTexto,
        String ctaLink,
        Integer orden,
        Boolean activo
) {}