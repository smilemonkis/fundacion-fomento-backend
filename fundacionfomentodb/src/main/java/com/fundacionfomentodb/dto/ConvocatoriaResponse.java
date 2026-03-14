package com.fundacionfomentodb.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ConvocatoriaResponse(
        Integer       id,
        String        titulo,
        String        descripcion,
        LocalDate     fechaInicio,
        LocalDate     fechaFin,
        Boolean       activa,
        String        estado,
        String        imagenUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}