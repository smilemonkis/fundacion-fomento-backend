package com.fundacionfomentodb.dto;

public record ImagenRequest(
        String url,
        String descripcion,
        Integer orden
) {}