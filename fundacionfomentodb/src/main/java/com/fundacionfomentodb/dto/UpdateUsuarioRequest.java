package com.fundacionfomentodb.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUsuarioRequest(
        @Email(message = "El correo debe ser válido")
        String email,

        @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
        String password,

        String rol
) {
}
