package com.fundacionfomentodb.service;

import com.fundacionfomentodb.dto.CreateConvocatoriaRequest;
import com.fundacionfomentodb.dto.UpdateConvocatoriaRequest;
import com.fundacionfomentodb.dto.ConvocatoriaResponse;
import com.fundacionfomentodb.entity.Convocatoria;
import com.fundacionfomentodb.repository.ConvocatoriaRepository;
import com.fundacionfomentodb.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ConvocatoriaService {

    private final ConvocatoriaRepository convocatoriaRepository;

    public ConvocatoriaResponse crearConvocatoria(CreateConvocatoriaRequest request) {
        Convocatoria convocatoria = Convocatoria.builder()
                .titulo(request.titulo())
                .descripcion(request.descripcion())
                .fechaInicio(request.fechaInicio())
                .fechaFin(request.fechaFin())
                .activa(true)
                .build();

        Convocatoria convocatoriaGuardada = convocatoriaRepository.save(convocatoria);
        return toResponseDto(convocatoriaGuardada);
    }

    @Transactional(readOnly = true)
    public ConvocatoriaResponse obtenerConvocatoriaPorId(Integer id) {
        return convocatoriaRepository.findById(id)
                .map(this::toResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Convocatoria no encontrada"));
    }

    @Transactional(readOnly = true)
    public Page<ConvocatoriaResponse> listarConvocatorias(Boolean activa, Pageable pageable) {
        Page<Convocatoria> resultado;
        
        if (activa != null) {
            resultado = convocatoriaRepository.findByActiva(activa, pageable);
        } else {
            resultado = convocatoriaRepository.findAll(pageable);
        }
        
        return resultado.map(this::toResponseDto);
    }

    @Transactional(readOnly = true)
    public Page<ConvocatoriaResponse> listarConvocatoriasActivas(Pageable pageable) {
        return convocatoriaRepository.findByActiva(true, pageable)
                .map(this::toResponseDto);
    }

    public ConvocatoriaResponse actualizarConvocatoria(Integer id, UpdateConvocatoriaRequest request) {
        Convocatoria convocatoria = convocatoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Convocatoria no encontrada"));

        if (request.titulo() != null && !request.titulo().isBlank()) {
            convocatoria.setTitulo(request.titulo());
        }

        if (request.descripcion() != null && !request.descripcion().isBlank()) {
            convocatoria.setDescripcion(request.descripcion());
        }

        if (request.fechaInicio() != null) {
            convocatoria.setFechaInicio(request.fechaInicio());
        }

        if (request.fechaFin() != null) {
            convocatoria.setFechaFin(request.fechaFin());
        }

        Convocatoria convocatoriaActualizada = convocatoriaRepository.save(convocatoria);
        return toResponseDto(convocatoriaActualizada);
    }

    public ConvocatoriaResponse activarConvocatoria(Integer id) {
        Convocatoria convocatoria = convocatoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Convocatoria no encontrada"));
        
        convocatoria.setActiva(true);
        Convocatoria convocatoriaActualizada = convocatoriaRepository.save(convocatoria);
        return toResponseDto(convocatoriaActualizada);
    }

    public ConvocatoriaResponse desactivarConvocatoria(Integer id) {
        Convocatoria convocatoria = convocatoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Convocatoria no encontrada"));
        
        convocatoria.setActiva(false);
        Convocatoria convocatoriaActualizada = convocatoriaRepository.save(convocatoria);
        return toResponseDto(convocatoriaActualizada);
    }

    public void eliminarConvocatoria(Integer id) {
        Convocatoria convocatoria = convocatoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Convocatoria no encontrada"));
        
        convocatoriaRepository.delete(convocatoria);
    }

    private ConvocatoriaResponse toResponseDto(Convocatoria convocatoria) {
        return new ConvocatoriaResponse(
                convocatoria.getId(),
                convocatoria.getTitulo(),
                convocatoria.getDescripcion(),
                convocatoria.getFechaInicio(),
                convocatoria.getFechaFin(),
                convocatoria.getActiva(),
                convocatoria.getCreatedAt(),
                convocatoria.getUpdatedAt()
        );
    }
}
