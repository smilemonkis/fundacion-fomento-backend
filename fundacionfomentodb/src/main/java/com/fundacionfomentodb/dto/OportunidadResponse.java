package com.fundacionfomentodb.dto;

public record OportunidadResponse(
        Integer id,
        String titulo,
        String descripcion,
        String imagenUrl,
        String tipo,
        String fechaLimite,
        Boolean activo,
        String enlace,
        String createdAt,
        String updatedAt
) {}