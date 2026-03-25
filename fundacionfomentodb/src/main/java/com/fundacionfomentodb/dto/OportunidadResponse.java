package com.fundacionfomentodb.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record OportunidadResponse(
        Integer       id,
        String        titulo,
        String        descripcion,
        String        imagenUrl,
        String        tipo,
        LocalDate     fechaLimite,
        Boolean       activo,
        String        enlace,
        String        estado,
        String        textoBoton,
        Boolean       mostrarBoton,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}