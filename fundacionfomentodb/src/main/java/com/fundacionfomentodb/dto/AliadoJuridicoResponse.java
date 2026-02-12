package com.fundacionfomentodb.dto;

import lombok.Builder;

@Builder
public record AliadoJuridicoResponse(
        Integer id,
        Integer usuarioId,
        String nit,
        String razonSocial,
        String representante,
        String email,
        String telefono,
        String direccion,
        String createdAt,
        String updatedAt
) {}

