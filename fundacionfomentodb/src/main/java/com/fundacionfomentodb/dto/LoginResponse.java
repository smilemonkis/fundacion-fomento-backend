package com.fundacionfomentodb.dto;

public record LoginResponse(
        String token,
        Integer userId,
        String nombre,       // Se usará para Aliado Natural
        String apellido,     // Se usará para Aliado Natural
        String razonSocial,  // <--- NUEVO: Para Aliado Jurídico
        String nit,          // <--- NUEVO: Para Aliado Jurídico
        String email,
        String rol,
        Boolean activo
) {
}