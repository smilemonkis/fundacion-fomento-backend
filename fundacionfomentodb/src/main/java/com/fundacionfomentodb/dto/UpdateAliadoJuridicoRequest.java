package com.fundacionfomentodb.dto;

import jakarta.validation.constraints.Email;

public record UpdateAliadoJuridicoRequest(
        @Email(message = "El correo debe ser válido")
        String email,

        String razonSocial,

        String representante,

        String telefono,

        String direccion
) {
}
