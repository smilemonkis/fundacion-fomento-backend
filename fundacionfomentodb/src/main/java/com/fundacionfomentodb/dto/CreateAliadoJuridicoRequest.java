package com.fundacionfomentodb.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateAliadoJuridicoRequest(
        @NotBlank(message = "El correo es requerido")
        @Email(message = "El correo debe ser válido")
        String email,

        @NotBlank(message = "La contraseña es requerida")
        @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
        String password,

        @NotBlank(message = "El NIT es requerido")
        String nit,

        @NotBlank(message = "La razón social es requerida")
        String razonSocial,

        @NotBlank(message = "El representante es requerido")
        String representante,

        @NotBlank(message = "El teléfono es requerido")
        String telefono,

        @NotBlank(message = "La dirección es requerida")
        String direccion
) {
}
