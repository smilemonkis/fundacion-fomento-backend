package com.fundacionfomentodb.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateAliadoDestacadoRequest(
        @NotBlank String nombre,
        @NotBlank String logoUrl,
        String  sitioWeb,
        Integer orden
) {}