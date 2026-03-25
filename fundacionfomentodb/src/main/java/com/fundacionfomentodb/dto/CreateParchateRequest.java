package com.fundacionfomentodb.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

public record CreateParchateRequest(
        @NotBlank(message = "El título es requerido")   String titulo,
        @NotBlank(message = "La descripción es requerida") String descripcion,
        String imagenUrl,
        List<ImagenRequest> galeria,
        @NotBlank(message = "El tipo es requerido")     String tipo,
        @NotBlank(message = "La ubicación es requerida") String ubicacion,
        String direccion,
        String urlMapa,
        String enlace,
        LocalDateTime fechaEvento
) {}