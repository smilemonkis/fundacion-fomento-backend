package com.fundacionfomentodb.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public record CreateOportunidadRequest(
        @NotBlank String titulo,
        @NotBlank String descripcion,
        String    imagenUrl,
        @NotBlank String tipo,
        LocalDate fechaLimite,
        String    enlace,
        String    estado,
        String    textoBoton,
        Boolean   mostrarBoton
) {}