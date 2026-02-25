package com.fundacionfomentodb.dto;

public record LoginResponse(
        String token,
        Integer userId,
        String nombre,
        String apellido,
        String email,
        String rol,
        Boolean activo
) {
}
