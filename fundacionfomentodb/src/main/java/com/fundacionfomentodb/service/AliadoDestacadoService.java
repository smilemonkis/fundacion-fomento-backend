package com.fundacionfomentodb.service;

import com.fundacionfomentodb.dto.*;
import com.fundacionfomentodb.entity.AliadoDestacado;
import com.fundacionfomentodb.exception.ResourceNotFoundException;
import com.fundacionfomentodb.repository.AliadoDestacadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AliadoDestacadoService {

    private final AliadoDestacadoRepository repo;

    public AliadoDestacadoResponse crear(CreateAliadoDestacadoRequest req) {
        AliadoDestacado a = AliadoDestacado.builder()
                .nombre(req.nombre())
                .logoUrl(req.logoUrl())
                .sitioWeb(req.sitioWeb())
                .orden(req.orden() != null ? req.orden() : 0)
                .activo(true)
                .build();
        return toDto(repo.save(a));
    }

    @Transactional(readOnly = true)
    public List<AliadoDestacadoResponse> listarActivos() {
        return repo.findByActivoTrueOrderByOrdenAsc().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<AliadoDestacadoResponse> listarTodos() {
        return repo.findAllByOrderByOrdenAsc().stream().map(this::toDto).toList();
    }

    public AliadoDestacadoResponse actualizar(Integer id, UpdateAliadoDestacadoRequest req) {
        AliadoDestacado a = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aliado destacado no encontrado"));
        if (req.nombre()  != null) a.setNombre(req.nombre());
        if (req.logoUrl() != null) a.setLogoUrl(req.logoUrl());
        if (req.sitioWeb()!= null) a.setSitioWeb(req.sitioWeb());
        if (req.orden()   != null) a.setOrden(req.orden());
        if (req.activo()  != null) a.setActivo(req.activo());
        return toDto(repo.save(a));
    }

    public void eliminar(Integer id) {
        repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Aliado destacado no encontrado"));
        repo.deleteById(id);
    }

    private AliadoDestacadoResponse toDto(AliadoDestacado a) {
        return new AliadoDestacadoResponse(
                a.getId(), a.getNombre(), a.getLogoUrl(),
                a.getSitioWeb(), a.getOrden(), a.getActivo()
        );
    }
}