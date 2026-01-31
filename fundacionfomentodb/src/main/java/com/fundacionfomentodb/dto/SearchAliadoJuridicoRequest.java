package com.fundacionfomentodb.dto;

public record SearchAliadoJuridicoRequest(
        String nit,
        String razonSocial,
        String email,
        Integer pageNumber,
        Integer pageSize
) {
}
