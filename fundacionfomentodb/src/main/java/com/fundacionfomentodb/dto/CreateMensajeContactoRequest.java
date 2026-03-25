package com.fundacionfomentodb.dto;

import jakarta.validation.constraints.*;

public record CreateMensajeContactoRequest(
        @NotBlank String nombre,
        String telefono,
        @NotBlank @Email String correo,
        @NotBlank String mensaje
) {}