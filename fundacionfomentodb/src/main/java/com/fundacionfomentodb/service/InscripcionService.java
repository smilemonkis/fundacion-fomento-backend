package com.fundacionfomentodb.service;

import com.fundacionfomentodb.dto.CreateInscripcionRequest;
import com.fundacionfomentodb.dto.UpdateInscripcionRequest;
import com.fundacionfomentodb.dto.InscripcionResponse;
import com.fundacionfomentodb.entity.Convocatoria;
import com.fundacionfomentodb.entity.Inscripcion;
import com.fundacionfomentodb.entity.Usuario;
import com.fundacionfomentodb.repository.ConvocatoriaRepository;
import com.fundacionfomentodb.repository.InscripcionRepository;
import com.fundacionfomentodb.repository.UsuarioRepository;
import com.fundacionfomentodb.exception.BadRequestException;
import com.fundacionfomentodb.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class InscripcionService {

    private final InscripcionRepository inscripcionRepository;
    private final UsuarioRepository usuarioRepository;
    private final ConvocatoriaRepository convocatoriaRepository;

    public InscripcionResponse crearInscripcion(CreateInscripcionRequest request) {
        Usuario usuario = usuarioRepository.findById(request.usuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (!usuario.getActivo()) {
            throw new BadRequestException("El usuario no está activo");
        }

        Convocatoria convocatoria = convocatoriaRepository.findById(request.convocatoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Convocatoria no encontrada"));

        if (!convocatoria.getActiva()) {
            throw new BadRequestException("La convocatoria no está activa");
        }

        if (convocatoria.getFechaFin().isBefore(LocalDate.now())) {
            throw new BadRequestException("La convocatoria ya ha terminado");
        }

        // Verificar si ya existe una inscripción para este usuario en esta convocatoria
        if (inscripcionRepository.existsByUsuarioIdAndConvocatoriaId(request.usuarioId(), request.convocatoriaId())) {
            throw new BadRequestException("El usuario ya está inscrito en esta convocatoria");
        }

        Inscripcion inscripcion = Inscripcion.builder()
                .usuario(usuario)
                .convocatoria(convocatoria)
                .estado(Inscripcion.EstadoEnum.PENDIENTE)
                .observaciones(request.observaciones())
                .build();

        Inscripcion inscripcionGuardada = inscripcionRepository.save(inscripcion);
        return toResponseDto(inscripcionGuardada);
    }

    @Transactional(readOnly = true)
    public InscripcionResponse obtenerInscripcionPorId(Integer id) {
        return inscripcionRepository.findById(id)
                .map(this::toResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Inscripción no encontrada"));
    }

    @Transactional(readOnly = true)
    public Page<InscripcionResponse> listarInscripciones(Integer usuarioId, Integer convocatoriaId, String estado, Pageable pageable) {
        Page<Inscripcion> resultado;
        
        if (usuarioId != null) {
            resultado = inscripcionRepository.findByUsuarioId(usuarioId, pageable);
        } else if (convocatoriaId != null) {
            resultado = inscripcionRepository.findByConvocatoriaId(convocatoriaId, pageable);
        } else if (estado != null && !estado.isBlank()) {
            resultado = inscripcionRepository.findByEstado(Inscripcion.EstadoEnum.valueOf(estado), pageable);
        } else {
            resultado = inscripcionRepository.findAll(pageable);
        }
        
        return resultado.map(this::toResponseDto);
    }

    @Transactional(readOnly = true)
    public Page<InscripcionResponse> listarInscripcionesPorUsuario(Integer usuarioId, Pageable pageable) {
        return inscripcionRepository.findByUsuarioId(usuarioId, pageable)
                .map(this::toResponseDto);
    }

    @Transactional(readOnly = true)
    public Page<InscripcionResponse> listarInscripcionesPorConvocatoria(Integer convocatoriaId, Pageable pageable) {
        return inscripcionRepository.findByConvocatoriaId(convocatoriaId, pageable)
                .map(this::toResponseDto);
    }

    public InscripcionResponse actualizarInscripcion(Integer id, UpdateInscripcionRequest request) {
        Inscripcion inscripcion = inscripcionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inscripción no encontrada"));

        if (request.observaciones() != null) {
            inscripcion.setObservaciones(request.observaciones());
        }

        Inscripcion inscripcionActualizada = inscripcionRepository.save(inscripcion);
        return toResponseDto(inscripcionActualizada);
    }

    public InscripcionResponse aprobarInscripcion(Integer id) {
        Inscripcion inscripcion = inscripcionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inscripción no encontrada"));
        
        if (inscripcion.getEstado() != Inscripcion.EstadoEnum.PENDIENTE) {
            throw new BadRequestException("Solo se pueden aprobar inscripciones pendientes");
        }
        
        inscripcion.setEstado(Inscripcion.EstadoEnum.ACEPTADO);
        Inscripcion inscripcionActualizada = inscripcionRepository.save(inscripcion);
        return toResponseDto(inscripcionActualizada);
    }

    public InscripcionResponse rechazarInscripcion(Integer id) {
        Inscripcion inscripcion = inscripcionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inscripción no encontrada"));
        
        if (inscripcion.getEstado() != Inscripcion.EstadoEnum.PENDIENTE) {
            throw new BadRequestException("Solo se pueden rechazar inscripciones pendientes");
        }
        
        inscripcion.setEstado(Inscripcion.EstadoEnum.RECHAZADO);
        Inscripcion inscripcionActualizada = inscripcionRepository.save(inscripcion);
        return toResponseDto(inscripcionActualizada);
    }

    public void eliminarInscripcion(Integer id) {
        Inscripcion inscripcion = inscripcionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inscripción no encontrada"));
        
        inscripcionRepository.delete(inscripcion);
    }

    private InscripcionResponse toResponseDto(Inscripcion inscripcion) {
        return new InscripcionResponse(
                inscripcion.getId(),
                inscripcion.getUsuario().getId(),
                inscripcion.getUsuario().getEmail(),
                inscripcion.getConvocatoria().getId(),
                inscripcion.getConvocatoria().getTitulo(),
                inscripcion.getEstado().name(),
                inscripcion.getObservaciones(),
                inscripcion.getCreatedAt(),
                inscripcion.getUpdatedAt()
        );
    }
}
