package com.fundacionfomentodb.service;

import com.fundacionfomentodb.dto.*;
import com.fundacionfomentodb.entity.Convocatoria;
import com.fundacionfomentodb.exception.BadRequestException;
import com.fundacionfomentodb.exception.ResourceNotFoundException;
import com.fundacionfomentodb.repository.ConvocatoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ConvocatoriaService {

    private final ConvocatoriaRepository convocatoriaRepository;

    public ConvocatoriaResponse crearConvocatoria(CreateConvocatoriaRequest req) {
        validarFechas(req.fechaInicio(), req.fechaFin());
        Convocatoria c = Convocatoria.builder()
                .titulo(req.titulo())
                .descripcion(req.descripcion())
                .fechaInicio(req.fechaInicio())
                .fechaFin(req.fechaFin())
                .activa(true)
                .estado(parseEstado(req.estado(), Convocatoria.EstadoEnum.ABIERTO))
                .imagenUrl(req.imagenUrl())
                .enlace(req.enlace())
                .textoBoton(req.textoBoton() != null ? req.textoBoton() : "Inscribirme")
                .mostrarBoton(req.mostrarBoton() != null ? req.mostrarBoton() : true)
                .build();
        return toDto(convocatoriaRepository.save(c));
    }

    @Transactional(readOnly = true)
    public ConvocatoriaResponse obtenerConvocatoriaPorId(Integer id) {
        return convocatoriaRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Convocatoria no encontrada"));
    }

    @Transactional(readOnly = true)
    public Page<ConvocatoriaResponse> listarConvocatorias(Boolean activa, Pageable pageable) {
        Page<Convocatoria> resultado = activa != null
                ? convocatoriaRepository.findByActiva(activa, pageable)
                : convocatoriaRepository.findAll(pageable);
        return resultado.map(this::toDto);
    }

    @Transactional(readOnly = true)
    public Page<ConvocatoriaResponse> listarConvocatoriasActivas(Pageable pageable) {
        return convocatoriaRepository.findByActiva(true, pageable).map(this::toDto);
    }

    public ConvocatoriaResponse actualizarConvocatoria(Integer id, UpdateConvocatoriaRequest req) {
        Convocatoria c = convocatoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Convocatoria no encontrada"));

        if (req.titulo()       != null) c.setTitulo(req.titulo());
        if (req.descripcion()  != null) c.setDescripcion(req.descripcion());
        if (req.imagenUrl()    != null) c.setImagenUrl(req.imagenUrl());
        if (req.activa()       != null) c.setActiva(req.activa());
        if (req.estado()       != null) c.setEstado(Convocatoria.EstadoEnum.valueOf(req.estado()));
        if (req.enlace()       != null) c.setEnlace(req.enlace());
        if (req.textoBoton()   != null) c.setTextoBoton(req.textoBoton());
        if (req.mostrarBoton() != null) c.setMostrarBoton(req.mostrarBoton());

        var newInicio = req.fechaInicio() != null ? req.fechaInicio() : c.getFechaInicio();
        var newFin    = req.fechaFin()    != null ? req.fechaFin()    : c.getFechaFin();
        validarFechas(newInicio, newFin);
        c.setFechaInicio(newInicio);
        c.setFechaFin(newFin);

        return toDto(convocatoriaRepository.save(c));
    }

    public ConvocatoriaResponse activarConvocatoria(Integer id) {
        Convocatoria c = convocatoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Convocatoria no encontrada"));
        c.setActiva(true);
        return toDto(convocatoriaRepository.save(c));
    }

    public ConvocatoriaResponse desactivarConvocatoria(Integer id) {
        Convocatoria c = convocatoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Convocatoria no encontrada"));
        c.setActiva(false);
        return toDto(convocatoriaRepository.save(c));
    }

    public void eliminarConvocatoria(Integer id) {
        Convocatoria c = convocatoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Convocatoria no encontrada"));
        convocatoriaRepository.delete(c);
    }

    private void validarFechas(java.time.LocalDate inicio, java.time.LocalDate fin) {
        if (inicio != null && fin != null && !fin.isAfter(inicio))
            throw new BadRequestException("La fecha de fin debe ser posterior a la fecha de inicio");
    }

    private Convocatoria.EstadoEnum parseEstado(String estado, Convocatoria.EstadoEnum defecto) {
        if (estado == null) return defecto;
        try { return Convocatoria.EstadoEnum.valueOf(estado); }
        catch (Exception e) { return defecto; }
    }

    private ConvocatoriaResponse toDto(Convocatoria c) {
        return new ConvocatoriaResponse(
                c.getId(), c.getTitulo(), c.getDescripcion(),
                c.getFechaInicio(), c.getFechaFin(), c.getActiva(),
                c.getEstado().name(), c.getImagenUrl(),
                c.getEnlace(), c.getTextoBoton(), c.getMostrarBoton(),
                c.getCreatedAt(), c.getUpdatedAt()
        );
    }
}