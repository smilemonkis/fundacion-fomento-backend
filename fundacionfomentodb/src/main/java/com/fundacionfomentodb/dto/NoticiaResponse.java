package com.fundacionfomentodb.dto;

import java.util.List;

public record NoticiaResponse(
        Integer id,
        String titulo,
        String resumen,
        String contenido,
        String imagenUrl,
        List<ImagenDto> galeria,
        String autor,
        Boolean publicado,
        String fechaPublicacion,
        String createdAt,
        String updatedAt
) {}