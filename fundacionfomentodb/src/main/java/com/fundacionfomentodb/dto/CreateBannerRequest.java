package com.fundacionfomentodb.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateBannerRequest(
        @NotBlank String titulo,
        @NotBlank String subtitulo,
        @NotBlank String imagenUrl,
        @NotBlank String ctaTexto,
        @NotBlank String ctaLink,
        Integer orden
) {}