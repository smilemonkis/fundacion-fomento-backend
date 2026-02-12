package com.fundacionfomentodb.dto;

public record SearchAliadoNaturalRequest(
        String documento,
        String nombre,
        String email,
        Integer pageNumber,
        Integer pageSize
) {
}
