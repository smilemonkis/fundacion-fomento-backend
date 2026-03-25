package com.fundacionfomentodb.dto;

public record MensajeContactoResponse(
        Integer id,
        String  nombre,
        String  telefono,
        String  correo,
        String  mensaje,
        String  estado,
        String  createdAt
) {}