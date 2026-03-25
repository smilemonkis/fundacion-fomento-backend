package com.fundacionfomentodb.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

public record CreateParchateRequest(
        @NotBlank String titulo,
        @NotBlank String descripcion,
        String imagenUrl,
        List<ImagenRequest> galeria,
        @NotBlank String tipo,
        @NotBlank String ubicacion,
        String direccion,
        String urlMapa,
        String enlace,
        LocalDateTime fechaEvento,
        Boolean activo,
        String textoBoton,
        Boolean mostrarBoton
) {}