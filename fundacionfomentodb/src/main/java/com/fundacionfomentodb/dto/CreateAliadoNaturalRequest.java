package com.fundacionfomentodb.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateAliadoNaturalRequest(
        @NotBlank(message = "El correo es requerido")
        @Email(message = "El correo debe ser válido")
        String email,

        @NotBlank(message = "La contraseña es requerida")
        @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
        String password,

        @NotBlank(message = "El documento es requerido")
        String documento,

        @NotNull(message = "El tipo de documento es requerido")
        String tipoDocumento,

        @NotBlank(message = "El nombre es requerido")
        String nombre,

        @NotBlank(message = "El teléfono es requerido")
        String telefono,

        @NotBlank(message = "La dirección es requerida")
        String direccion
) {
}
