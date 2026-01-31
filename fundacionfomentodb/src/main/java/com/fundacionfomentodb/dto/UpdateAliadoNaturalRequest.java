package com.fundacionfomentodb.dto;

import jakarta.validation.constraints.*;

public record UpdateAliadoNaturalRequest(
        @Email(message = "El correo debe ser válido")
        String email,

        String nombre,

        String telefono,

        String direccion
) {}

