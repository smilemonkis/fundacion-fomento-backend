package com.fundacionfomentodb.dto;

public record UpdateBannerRequest(
        String titulo,
        String subtitulo,
        String imagenUrl,
        String ctaTexto,
        String ctaLink,
        String ctaColor,
        Integer orden,
        Boolean activo
) {}