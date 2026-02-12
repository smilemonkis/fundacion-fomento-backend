package com.fundacionfomentodb.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "El correo es requerido")
        @Email(message = "El correo debe ser válido")
        String email,

        @NotBlank(message = "La contraseña es requerida")
        String password
) {
}
