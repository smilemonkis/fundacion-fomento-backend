package com.fundacionfomentodb.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record CreateDonacionPublicaRequest(
        @NotNull @DecimalMin("1000") BigDecimal monto,
        @NotBlank String destino,
        Integer proyectoId,
        @NotBlank String donanteNombre,
        @NotBlank @Email String donanteEmail
) {}