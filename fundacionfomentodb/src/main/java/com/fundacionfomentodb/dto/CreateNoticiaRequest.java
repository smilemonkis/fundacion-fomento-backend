package com.fundacionfomentodb.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public record CreateNoticiaRequest(
        @NotBlank(message = "El título es requerido")
        String titulo,

        @NotBlank(message = "El resumen es requerido")
        String resumen,

        @NotBlank(message = "El contenido es requerido")
        String contenido,

        String imagenUrl,
        List<ImagenRequest> galeria,

        @NotBlank(message = "El autor es requerido")
        String autor,

        Boolean publicado,

        @NotNull(message = "La fecha de publicación es requerida")
        LocalDate fechaPublicacion
) {}