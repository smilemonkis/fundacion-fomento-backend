package com.fundacionfomentodb.dto;

import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;

public record UpdateDonacionRequest(
        @DecimalMin(value = "0.01", message = "El monto debe ser mayor a cero")
        BigDecimal monto,

        String destino,

        Integer proyectoId
) {
}
