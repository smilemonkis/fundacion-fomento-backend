package com.fundacionfomentodb.service;

import com.fundacionfomentodb.dto.*;
import com.fundacionfomentodb.entity.Parchate;
import com.fundacionfomentodb.entity.ParchateImagen;
import com.fundacionfomentodb.exception.ResourceNotFoundException;
import com.fundacionfomentodb.repository.ParchateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ParchateService {

    private final ParchateRepository parchateRepository;

    public ParchateResponse crear(CreateParchateRequest req) {
        Parchate p = Parchate.builder()
                .titulo(req.titulo())
                .descripcion(req.descripcion())
                .imagenUrl(req.imagenUrl())
                .tipo(req.tipo())
                .ubicacion(req.ubicacion())
                .direccion(req.direccion())
                .urlMapa(req.urlMapa())
                .enlace(req.enlace())
                .fechaEvento(req.fechaEvento())
                .activo(req.activo() != null ? req.activo() : true)
                .textoBoton(req.textoBoton() != null ? req.textoBoton() : "Más información")
                .mostrarBoton(req.mostrarBoton() != null ? req.mostrarBoton() : false)
                .build();
        agregarGaleria(p, req.galeria());
        return toDto(parchateRepository.save(p));
    }

    @Transactional(readOnly = true)
    public ParchateResponse obtenerPorId(Integer id) {
        return parchateRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada"));
    }

    @Transactional(readOnly = true)
    public Page<ParchateResponse> listar(Boolean activo, String tipo, String ubicacion, Pageable pageable) {
        boolean filtrarActivo = activo != null ? activo : true;
        Page<Parchate> page;
        if (tipo != null && !tipo.isBlank() && ubicacion != null && !ubicacion.isBlank()) {
            page = parchateRepository.findByTipoIgnoreCaseAndUbicacionIgnoreCaseAndActivo(tipo, ubicacion, filtrarActivo, pageable);
        } else if (tipo != null && !tipo.isBlank()) {
            page = parchateRepository.findByTipoIgnoreCaseAndActivo(tipo, filtrarActivo, pageable);
        } else if (ubicacion != null && !ubicacion.isBlank()) {
            page = parchateRepository.findByUbicacionIgnoreCaseAndActivo(ubicacion, filtrarActivo, pageable);
        } else if (activo != null) {
            page = parchateRepository.findByActivo(activo, pageable);
        } else {
            page = parchateRepository.findAll(pageable);
        }
        return page.map(this::toDto);
    }

    @Transactional(readOnly = true)
    public List<String> listarTipos() {
        return parchateRepository.findTiposActivos();
    }

    @Transactional(readOnly = true)
    public List<String> listarUbicaciones() {
        return parchateRepository.findUbicacionesActivas();
    }

    public ParchateResponse actualizar(Integer id, UpdateParchateRequest req) {
        Parchate p = parchateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada"));

        if (req.titulo()       != null) p.setTitulo(req.titulo());
        if (req.descripcion()  != null) p.setDescripcion(req.descripcion());
        if (req.imagenUrl()    != null) p.setImagenUrl(req.imagenUrl());
        if (req.tipo()         != null) p.setTipo(req.tipo());
        if (req.ubicacion()    != null) p.setUbicacion(req.ubicacion());
        if (req.direccion()    != null) p.setDireccion(req.direccion());
        if (req.urlMapa()      != null) p.setUrlMapa(req.urlMapa());
        if (req.enlace()       != null) p.setEnlace(req.enlace());
        if (req.fechaEvento()  != null) p.setFechaEvento(req.fechaEvento());
        if (req.activo()       != null) p.setActivo(req.activo());
        if (req.textoBoton()   != null) p.setTextoBoton(req.textoBoton());
        if (req.mostrarBoton() != null) p.setMostrarBoton(req.mostrarBoton());

        if (req.galeria() != null) {
            p.getGaleria().clear();
            agregarGaleria(p, req.galeria());
        }
        return toDto(parchateRepository.save(p));
    }

    public void eliminar(Integer id) {
        Parchate p = parchateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada"));
        parchateRepository.delete(p);
    }

    private void agregarGaleria(Parchate parchate, List<ImagenRequest> lista) {
        if (lista == null || lista.isEmpty()) return;
        lista.forEach(img -> parchate.getGaleria().add(
                ParchateImagen.builder()
                        .parchate(parchate)
                        .url(img.url())
                        .descripcion(img.descripcion())
                        .orden(img.orden() != null ? img.orden() : 0)
                        .build()
        ));
    }

    private ParchateResponse toDto(Parchate p) {
        List<ImagenDto> galeria = p.getGaleria().stream()
                .sorted((a, b) -> a.getOrden().compareTo(b.getOrden()))
                .map(img -> new ImagenDto(img.getId(), img.getUrl(), img.getDescripcion(), img.getOrden()))
                .collect(Collectors.toList());

        return new ParchateResponse(
                p.getId(), p.getTitulo(), p.getDescripcion(), p.getImagenUrl(),
                galeria, p.getTipo(), p.getUbicacion(), p.getDireccion(),
                p.getUrlMapa(), p.getEnlace(),
                p.getFechaEvento() != null ? p.getFechaEvento().toString() : null,
                p.getActivo(), p.getTextoBoton(), p.getMostrarBoton(),
                p.getCreatedAt().toString(), p.getUpdatedAt().toString()
        );
    }
}