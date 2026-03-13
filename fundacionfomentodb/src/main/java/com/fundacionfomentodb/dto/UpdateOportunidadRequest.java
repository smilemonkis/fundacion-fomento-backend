package com.fundacionfomentodb.dto;

import java.time.LocalDate;

public record UpdateOportunidadRequest(
        String titulo,
        String descripcion,
        String imagenUrl,
        String tipo,
        LocalDate fechaLimite,
        Boolean activo,
        String enlace
) {}
