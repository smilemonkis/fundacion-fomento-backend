package com.fundacionfomentodb.service;

import com.fundacionfomentodb.dto.*;
import com.fundacionfomentodb.entity.Donacion;
import com.fundacionfomentodb.entity.Proyecto;
import com.fundacionfomentodb.exception.ResourceNotFoundException;
import com.fundacionfomentodb.repository.DonacionRepository;
import com.fundacionfomentodb.repository.ProyectoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DonacionService {

    private final DonacionRepository donacionRepository;
    private final ProyectoRepository proyectoRepository;

    // Donación pública sin cuenta
    public DonacionResponse crearPublica(CreateDonacionPublicaRequest req) {
        Proyecto proyecto = null;
        Donacion.DestinoEnum destino = Donacion.DestinoEnum.LIBRE_INVERSION;

        if ("PROYECTO_ACTIVO".equals(req.destino()) && req.proyectoId() != null) {
            proyecto = proyectoRepository.findById(req.proyectoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado"));
            destino = Donacion.DestinoEnum.PROYECTO_ACTIVO;
        }

        Donacion d = Donacion.builder()
                .usuario(null)
                .donanteNombre(req.donanteNombre())
                .donanteEmail(req.donanteEmail())
                .monto(req.monto())
                .destino(destino)
                .proyecto(proyecto)
                .estado(Donacion.EstadoEnum.PENDIENTE)
                .build();

        return toDto(donacionRepository.save(d));
    }

    @Transactional(readOnly = true)
    public Page<DonacionResponse> listarTodas(Pageable pageable) {
        return donacionRepository.findAll(pageable).map(this::toDto);
    }

    public DonacionResponse confirmar(Integer id) { return cambiarEstado(id, Donacion.EstadoEnum.COMPLETADA); }
    public DonacionResponse rechazar(Integer id)  { return cambiarEstado(id, Donacion.EstadoEnum.RECHAZADA);  }
    public DonacionResponse cancelar(Integer id)  { return cambiarEstado(id, Donacion.EstadoEnum.CANCELADA);  }

    private DonacionResponse cambiarEstado(Integer id, Donacion.EstadoEnum nuevoEstado) {
        Donacion d = donacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donación no encontrada"));
        d.setEstado(nuevoEstado);
        return toDto(donacionRepository.save(d));
    }

    private DonacionResponse toDto(Donacion d) {
        // Si dona con cuenta usa datos del usuario, si no usa los campos directos
        String email  = d.getUsuario() != null ? d.getUsuario().getEmail()  : d.getDonanteEmail();
        String nombre = d.getUsuario() != null ? d.getUsuario().getNombre() : d.getDonanteNombre();
        return new DonacionResponse(
                d.getId(),
                d.getUsuario() != null ? d.getUsuario().getId() : null,
                email,
                nombre,
                d.getDonanteEmail(),
                d.getMonto(),
                d.getDestino().name(),
                d.getProyecto() != null ? d.getProyecto().getId()     : null,
                d.getProyecto() != null ? d.getProyecto().getNombre() : null,
                d.getEstado().name(),
                d.getFecha() != null ? d.getFecha().toString() : null
        );
    }
}