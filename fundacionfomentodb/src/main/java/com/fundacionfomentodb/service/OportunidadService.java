package com.fundacionfomentodb.service;

import com.fundacionfomentodb.dto.*;
import com.fundacionfomentodb.entity.Oportunidad;
import com.fundacionfomentodb.exception.ResourceNotFoundException;
import com.fundacionfomentodb.repository.OportunidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OportunidadService {

    private final OportunidadRepository oportunidadRepository;

    public OportunidadResponse crear(CreateOportunidadRequest req) {
        Oportunidad o = Oportunidad.builder()
                .titulo(req.titulo())
                .descripcion(req.descripcion())
                .imagenUrl(req.imagenUrl())
                .tipo(Oportunidad.TipoEnum.valueOf(req.tipo()))
                .fechaLimite(req.fechaLimite())
                .enlace(req.enlace())
                .activo(true)
                .estado(parseEstado(req.estado(), Oportunidad.EstadoEnum.ABIERTO))
                .textoBoton(req.textoBoton() != null ? req.textoBoton() : "Aplicar")
                .mostrarBoton(req.mostrarBoton() != null ? req.mostrarBoton() : true)
                .build();
        return toDto(oportunidadRepository.save(o));
    }

    @Transactional(readOnly = true)
    public OportunidadResponse obtenerPorId(Integer id) {
        return oportunidadRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Oportunidad no encontrada"));
    }

    @Transactional(readOnly = true)
    public Page<OportunidadResponse> listar(String tipo, Boolean activo, Pageable pageable) {
        Page<Oportunidad> resultado;
        if (tipo != null && !tipo.isBlank() && activo != null) {
            resultado = oportunidadRepository.findByTipoAndActivo(Oportunidad.TipoEnum.valueOf(tipo), activo, pageable);
        } else if (tipo != null && !tipo.isBlank()) {
            resultado = oportunidadRepository.findByTipo(Oportunidad.TipoEnum.valueOf(tipo), pageable);
        } else if (activo != null) {
            resultado = oportunidadRepository.findByActivo(activo, pageable);
        } else {
            resultado = oportunidadRepository.findAll(pageable);
        }
        return resultado.map(this::toDto);
    }

    public OportunidadResponse actualizar(Integer id, UpdateOportunidadRequest req) {
        Oportunidad o = oportunidadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Oportunidad no encontrada"));

        if (req.titulo()       != null) o.setTitulo(req.titulo());
        if (req.descripcion()  != null) o.setDescripcion(req.descripcion());
        if (req.imagenUrl()    != null) o.setImagenUrl(req.imagenUrl());
        if (req.tipo()         != null) o.setTipo(Oportunidad.TipoEnum.valueOf(req.tipo()));
        if (req.fechaLimite()  != null) o.setFechaLimite(req.fechaLimite());
        if (req.activo()       != null) o.setActivo(req.activo());
        if (req.enlace()       != null) o.setEnlace(req.enlace());
        if (req.estado()       != null) o.setEstado(Oportunidad.EstadoEnum.valueOf(req.estado()));
        if (req.textoBoton()   != null) o.setTextoBoton(req.textoBoton());
        if (req.mostrarBoton() != null) o.setMostrarBoton(req.mostrarBoton());

        return toDto(oportunidadRepository.save(o));
    }

    public void eliminar(Integer id) {
        Oportunidad o = oportunidadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Oportunidad no encontrada"));
        oportunidadRepository.delete(o);
    }

    private Oportunidad.EstadoEnum parseEstado(String estado, Oportunidad.EstadoEnum defecto) {
        if (estado == null) return defecto;
        try { return Oportunidad.EstadoEnum.valueOf(estado); }
        catch (Exception e) { return defecto; }
    }

    private OportunidadResponse toDto(Oportunidad o) {
        return new OportunidadResponse(
                o.getId(), o.getTitulo(), o.getDescripcion(), o.getImagenUrl(),
                o.getTipo().name(), o.getFechaLimite(), o.getActivo(),
                o.getEnlace(), o.getEstado().name(),
                o.getTextoBoton(), o.getMostrarBoton(),
                o.getCreatedAt(), o.getUpdatedAt()
        );
    }
}