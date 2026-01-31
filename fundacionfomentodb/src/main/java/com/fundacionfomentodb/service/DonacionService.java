package com.fundacionfomentodb.service;

import com.fundacionfomentodb.dto.CreateDonacionRequest;
import com.fundacionfomentodb.dto.UpdateDonacionRequest;
import com.fundacionfomentodb.dto.DonacionResponse;
import com.fundacionfomentodb.entity.Donacion;
import com.fundacionfomentodb.entity.Proyecto;
import com.fundacionfomentodb.entity.Usuario;
import com.fundacionfomentodb.repository.DonacionRepository;
import com.fundacionfomentodb.repository.ProyectoRepository;
import com.fundacionfomentodb.repository.UsuarioRepository;
import com.fundacionfomentodb.exception.BadRequestException;
import com.fundacionfomentodb.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
public class DonacionService {

    private final DonacionRepository donacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProyectoRepository proyectoRepository;

    public DonacionResponse crearDonacion(CreateDonacionRequest request) {
        Usuario usuario = usuarioRepository.findById(request.usuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (!usuario.getActivo()) {
            throw new BadRequestException("El usuario no está activo");
        }

        if (request.monto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("El monto debe ser mayor a cero");
        }

        Proyecto proyecto = null;
        if (request.proyectoId() != null) {
            proyecto = proyectoRepository.findById(request.proyectoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado"));
            
            if (!proyecto.getActivo()) {
                throw new BadRequestException("El proyecto no está activo");
            }
        }

        Donacion donacion = Donacion.builder()
                .usuario(usuario)
                .monto(request.monto())
                .destino(Donacion.DestinoEnum.valueOf(request.destino()))
                .proyecto(proyecto)
                .estado(Donacion.EstadoEnum.PENDIENTE)
                .build();

        Donacion donacionGuardada = donacionRepository.save(donacion);
        return toResponseDto(donacionGuardada);
    }

    @Transactional(readOnly = true)
    public DonacionResponse obtenerDonacionPorId(Integer id) {
        return donacionRepository.findById(id)
                .map(this::toResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Donación no encontrada"));
    }

    @Transactional(readOnly = true)
    public Page<DonacionResponse> listarDonaciones(Integer usuarioId, Integer proyectoId, String estado, Pageable pageable) {
        Page<Donacion> resultado;
        
        if (usuarioId != null) {
            resultado = donacionRepository.findByUsuarioId(usuarioId, pageable);
        } else if (proyectoId != null) {
            resultado = donacionRepository.findByProyectoId(proyectoId, pageable);
        } else if (estado != null && !estado.isBlank()) {
            resultado = donacionRepository.findByEstado(Donacion.EstadoEnum.valueOf(estado), pageable);
        } else {
            resultado = donacionRepository.findAll(pageable);
        }
        
        return resultado.map(this::toResponseDto);
    }

    @Transactional(readOnly = true)
    public Page<DonacionResponse> listarDonacionesPorUsuario(Integer usuarioId, Pageable pageable) {
        return donacionRepository.findByUsuarioId(usuarioId, pageable)
                .map(this::toResponseDto);
    }

    @Transactional(readOnly = true)
    public Page<DonacionResponse> listarDonacionesPorProyecto(Integer proyectoId, Pageable pageable) {
        return donacionRepository.findByProyectoId(proyectoId, pageable)
                .map(this::toResponseDto);
    }

    public DonacionResponse actualizarDonacion(Integer id, UpdateDonacionRequest request) {
        Donacion donacion = donacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donación no encontrada"));

        if (request.monto() != null && request.monto().compareTo(BigDecimal.ZERO) > 0) {
            donacion.setMonto(request.monto());
        }

        if (request.destino() != null && !request.destino().isBlank()) {
            donacion.setDestino(Donacion.DestinoEnum.valueOf(request.destino()));
        }

        if (request.proyectoId() != null) {
            Proyecto proyecto = proyectoRepository.findById(request.proyectoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado"));
            donacion.setProyecto(proyecto);
        }

        Donacion donacionActualizada = donacionRepository.save(donacion);
        return toResponseDto(donacionActualizada);
    }

    public DonacionResponse aprobarDonacion(Integer id) {
        Donacion donacion = donacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donación no encontrada"));
        
        if (donacion.getEstado() != Donacion.EstadoEnum.PENDIENTE) {
            throw new BadRequestException("Solo se pueden aprobar donaciones pendientes");
        }
        
        donacion.setEstado(Donacion.EstadoEnum.COMPLETADA);
        Donacion donacionActualizada = donacionRepository.save(donacion);
        return toResponseDto(donacionActualizada);
    }

    public DonacionResponse rechazarDonacion(Integer id) {
        Donacion donacion = donacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donación no encontrada"));
        
        if (donacion.getEstado() != Donacion.EstadoEnum.PENDIENTE) {
            throw new BadRequestException("Solo se pueden rechazar donaciones pendientes");
        }
        
        donacion.setEstado(Donacion.EstadoEnum.RECHAZADA);
        Donacion donacionActualizada = donacionRepository.save(donacion);
        return toResponseDto(donacionActualizada);
    }

    public DonacionResponse cancelarDonacion(Integer id) {
        Donacion donacion = donacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donación no encontrada"));
        
        if (donacion.getEstado() == Donacion.EstadoEnum.COMPLETADA) {
            throw new BadRequestException("No se pueden cancelar donaciones completadas");
        }
        
        donacion.setEstado(Donacion.EstadoEnum.CANCELADA);
        Donacion donacionActualizada = donacionRepository.save(donacion);
        return toResponseDto(donacionActualizada);
    }

    private DonacionResponse toResponseDto(Donacion donacion) {
        return new DonacionResponse(
                donacion.getId(),
                donacion.getUsuario().getId(),
                donacion.getUsuario().getEmail(),
                donacion.getMonto(),
                donacion.getDestino().name(),
                donacion.getProyecto() != null ? donacion.getProyecto().getId() : null,
                donacion.getProyecto() != null ? donacion.getProyecto().getNombre() : null,
                donacion.getEstado().name(),
                donacion.getFecha()
        );
    }
}
