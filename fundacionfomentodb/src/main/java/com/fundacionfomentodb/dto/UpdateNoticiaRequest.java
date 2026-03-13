package com.fundacionfomentodb.dto;

import java.time.LocalDate;
import java.util.List;

public record UpdateNoticiaRequest(
        String titulo,
        String resumen,
        String contenido,
        String imagenUrl,
        List<ImagenRequest> galeria,
        String autor,
        Boolean publicado,
        LocalDate fechaPublicacion
) {}