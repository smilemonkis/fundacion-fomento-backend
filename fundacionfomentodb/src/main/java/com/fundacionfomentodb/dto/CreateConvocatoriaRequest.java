package com.fundacionfomentodb.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public record CreateConvocatoriaRequest(
        @NotBlank String titulo,
        @NotBlank String descripcion,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        String    estado,
        String    imagenUrl,
        String    enlace,
        String    textoBoton,
        Boolean   mostrarBoton
) {}