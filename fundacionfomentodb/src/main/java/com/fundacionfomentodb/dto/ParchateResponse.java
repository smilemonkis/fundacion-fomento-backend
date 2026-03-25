package com.fundacionfomentodb.dto;

import java.util.List;

public record ParchateResponse(
        Integer id,
        String titulo,
        String descripcion,
        String imagenUrl,
        List<ImagenDto> galeria,
        String tipo,
        String ubicacion,
        String direccion,
        String urlMapa,
        String enlace,
        String fechaEvento,
        Boolean activo,
        String createdAt,
        String updatedAt
) {}