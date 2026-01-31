package com.fundacionfomentodb.mapper;

import com.fundacionfomentodb.dto.*;
import com.fundacionfomentodb.entity.AliadoJuridico;
import org.springframework.stereotype.Component;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

@Component
public class AliadoJuridicoMapper {
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public Function<AliadoJuridico, AliadoJuridicoResponse> toResponseDto() {
        return aliado -> AliadoJuridicoResponse.builder()
            .id(aliado.getId())
            .usuarioId(aliado.getUsuario().getId())
            .nit(aliado.getNit())
            .razonSocial(aliado.getRazonSocial())
            .representante(aliado.getRepresentante())
            .email(aliado.getEmail())
            .telefono(aliado.getTelefono())
            .direccion(aliado.getDireccion())
            .createdAt(aliado.getCreatedAt().format(FORMATTER))
            .updatedAt(aliado.getUpdatedAt().format(FORMATTER))
            .build();
    }
    
    public Function<AliadoJuridico, AliadoJuridicoSimpleResponse> toSimpleResponseDto() {
        return aliado -> AliadoJuridicoSimpleResponse.builder()
            .id(aliado.getId())
            .nit(aliado.getNit())
            .razonSocial(aliado.getRazonSocial())
            .representante(aliado.getRepresentante())
            .email(aliado.getEmail())
            .build();
    }
    
    public Function<CreateAliadoJuridicoRequest, AliadoJuridico> toEntity() {
        return request -> AliadoJuridico.builder()
            .nit(request.nit())
            .razonSocial(request.razonSocial())
            .representante(request.representante())
            .email(request.email())
            .telefono(request.telefono())
            .direccion(request.direccion())
            .build();
    }
    
    public Function<UpdateAliadoJuridicoRequest, java.util.function.Consumer<AliadoJuridico>> toUpdateConsumer() {
        return request -> aliado -> {
            if (request.razonSocial() != null && !request.razonSocial().isBlank()) {
                aliado.setRazonSocial(request.razonSocial());
            }
            if (request.representante() != null && !request.representante().isBlank()) {
                aliado.setRepresentante(request.representante());
            }
            if (request.telefono() != null && !request.telefono().isBlank()) {
                aliado.setTelefono(request.telefono());
            }
            if (request.direccion() != null && !request.direccion().isBlank()) {
                aliado.setDireccion(request.direccion());
            }
            if (request.email() != null && !request.email().isBlank()) {
                aliado.setEmail(request.email());
                aliado.getUsuario().setEmail(request.email());
            }
        };
    }
    
    public AliadoJuridicoResponse toResponseDto(AliadoJuridico aliado) {
        return toResponseDto().apply(aliado);
    }
    
    public AliadoJuridicoSimpleResponse toSimpleResponseDto(AliadoJuridico aliado) {
        return toSimpleResponseDto().apply(aliado);
    }
    
    public AliadoJuridico toEntity(CreateAliadoJuridicoRequest request) {
        return toEntity().apply(request);
    }
    
    public void updateEntity(UpdateAliadoJuridicoRequest request, AliadoJuridico aliado) {
        toUpdateConsumer().apply(request).accept(aliado);
    }
}
