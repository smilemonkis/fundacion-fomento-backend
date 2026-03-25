package com.fundacionfomentodb.dto;

import java.time.LocalDateTime;
import java.util.List;

public record UpdateParchateRequest(
        String titulo,
        String descripcion,
        String imagenUrl,
        List<ImagenRequest> galeria,
        String tipo,
        String ubicacion,
        String direccion,
        String urlMapa,
        String enlace,
        LocalDateTime fechaEvento,
        Boolean activo
) {}