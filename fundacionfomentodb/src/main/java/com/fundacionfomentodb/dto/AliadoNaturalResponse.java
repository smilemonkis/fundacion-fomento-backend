package com.fundacionfomentodb.dto;

import lombok.Builder;

@Builder
public record AliadoNaturalResponse(
        Integer id,
        Integer usuarioId,
        String documento,
        String tipoDocumento,
        String nombre,
        String email,
        String telefono,
        String direccion,
        String createdAt,
        String updatedAt
) {
}
