package com.fundacionfomentodb.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CreateDonacionRequest(
        @NotNull(message = "El ID del usuario es requerido")
        Integer usuarioId,

        @NotNull(message = "El monto es requerido")
        @DecimalMin(value = "0.01", message = "El monto debe ser mayor a cero")
        BigDecimal monto,

        @NotNull(message = "El destino es requerido")
        String destino,

        Integer proyectoId
) {
}
