package com.fundacionfomentodb.dto;

import lombok.Builder;

@Builder
public record AliadoJuridicoSimpleResponse(
        Integer id,
        String nit,
        String razonSocial,
        String representante,
        String email
) {
}
