package com.fundacionfomentodb.dto;

import lombok.Builder;

@Builder
public record AliadoNaturalSimpleResponse(
        Integer id,
        String documento,
        String nombre,
        String email,
        String telefono
) {
}
