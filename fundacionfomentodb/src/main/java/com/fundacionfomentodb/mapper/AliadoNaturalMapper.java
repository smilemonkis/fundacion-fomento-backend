package com.fundacionfomentodb.mapper;

import com.fundacionfomentodb.dto.*;
import com.fundacionfomentodb.entity.AliadoNatural;
import org.springframework.stereotype.Component;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

@Component
public class AliadoNaturalMapper {
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public Function<AliadoNatural, AliadoNaturalResponse> toResponseDto() {
        return aliado -> AliadoNaturalResponse.builder()
            .id(aliado.getId())
            .usuarioId(aliado.getUsuario().getId())
            .documento(aliado.getDocumento())
            .tipoDocumento(aliado.getTipoDocumento().name())
            .nombre(aliado.getNombre())
            .email(aliado.getUsuario().getEmail())
            .telefono(aliado.getTelefono())
            .direccion(aliado.getDireccion())
            .createdAt(aliado.getCreatedAt().format(FORMATTER))
            .updatedAt(aliado.getUpdatedAt().format(FORMATTER))
            .build();
    }
    
    public Function<AliadoNatural, AliadoNaturalSimpleResponse> toSimpleResponseDto() {
        return aliado -> AliadoNaturalSimpleResponse.builder()
            .id(aliado.getId())
            .documento(aliado.getDocumento())
            .nombre(aliado.getNombre())
            .email(aliado.getUsuario().getEmail())
            .telefono(aliado.getTelefono())
            .build();
    }
    
    public Function<CreateAliadoNaturalRequest, AliadoNatural> toEntity() {
        return request -> AliadoNatural.builder()
            .documento(request.documento())
            .tipoDocumento(AliadoNatural.TipoDocumentoEnum.valueOf(request.tipoDocumento()))
            .nombre(request.nombre())
            .telefono(request.telefono())
            .direccion(request.direccion())
            .build();
    }
    
    public Function<UpdateAliadoNaturalRequest, java.util.function.Consumer<AliadoNatural>> toUpdateConsumer() {
        return request -> aliado -> {
            if (request.nombre() != null && !request.nombre().isBlank()) {
                aliado.setNombre(request.nombre());
            }
            if (request.telefono() != null && !request.telefono().isBlank()) {
                aliado.setTelefono(request.telefono());
            }
            if (request.direccion() != null && !request.direccion().isBlank()) {
                aliado.setDireccion(request.direccion());
            }
            if (request.email() != null && !request.email().isBlank()) {
                aliado.getUsuario().setEmail(request.email());
            }
        };
    }
    
    public AliadoNaturalResponse toResponseDto(AliadoNatural aliado) {
        return toResponseDto().apply(aliado);
    }
    
    public AliadoNaturalSimpleResponse toSimpleResponseDto(AliadoNatural aliado) {
        return toSimpleResponseDto().apply(aliado);
    }
    
    public AliadoNatural toEntity(CreateAliadoNaturalRequest request) {
        return toEntity().apply(request);
    }
    
    public void updateEntity(UpdateAliadoNaturalRequest request, AliadoNatural aliado) {
        toUpdateConsumer().apply(request).accept(aliado);
    }
}
