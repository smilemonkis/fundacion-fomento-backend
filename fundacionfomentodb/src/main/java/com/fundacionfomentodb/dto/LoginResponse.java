package com.fundacionfomentodb.dto;

public record LoginResponse(
        String token,
        Integer userId,
        String email,
        String rol,
        Boolean activo
) {
}
