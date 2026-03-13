package com.fundacionfomentodb.dto;

public record BannerResponse(
        Integer id,
        String titulo,
        String subtitulo,
        String imagenUrl,
        String ctaTexto,
        String ctaLink,
        String ctaColor,
        Integer orden,
        Boolean activo,
        String createdAt,
        String updatedAt
) {}