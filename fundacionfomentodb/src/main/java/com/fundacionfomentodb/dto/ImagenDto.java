package com.fundacionfomentodb.dto;

public record ImagenDto(
        Integer id,
        String url,
        String descripcion,
        Integer orden
) {}