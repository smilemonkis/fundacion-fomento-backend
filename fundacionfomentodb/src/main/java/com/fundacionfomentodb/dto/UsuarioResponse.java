package com.fundacionfomentodb.dto;

import java.time.LocalDateTime;

public record UsuarioResponse(
        Integer id,
        String email,
        String rol,
        Boolean activo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
