package com.fundacionfomentodb.service;

import com.fundacionfomentodb.dto.*;
import com.fundacionfomentodb.entity.Metrica;
import com.fundacionfomentodb.exception.ResourceNotFoundException;
import com.fundacionfomentodb.repository.MetricaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MetricaService {

    private final MetricaRepository repo;

    public MetricaResponse crear(MetricaRequest req) {
        Metrica m = Metrica.builder()
                .label(req.label())
                .valor(req.valor())
                .icono(req.icono() != null ? req.icono() : "Users")
                .orden(req.orden() != null ? req.orden() : 0)
                .activo(true)
                .build();
        return toDto(repo.save(m));
    }

    @Transactional(readOnly = true)
    public List<MetricaResponse> listarActivas() {
        return repo.findByActivoTrueOrderByOrdenAsc().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<MetricaResponse> listarTodas() {
        return repo.findAllByOrderByOrdenAsc().stream().map(this::toDto).toList();
    }

    public MetricaResponse actualizar(Integer id, MetricaRequest req) {
        Metrica m = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Métrica no encontrada"));
        if (req.label()  != null) m.setLabel(req.label());
        if (req.valor()  != null) m.setValor(req.valor());
        if (req.icono()  != null) m.setIcono(req.icono());
        if (req.orden()  != null) m.setOrden(req.orden());
        if (req.activo() != null) m.setActivo(req.activo());
        return toDto(repo.save(m));
    }

    public void eliminar(Integer id) {
        repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Métrica no encontrada"));
        repo.deleteById(id);
    }

    private MetricaResponse toDto(Metrica m) {
        return new MetricaResponse(m.getId(), m.getLabel(), m.getValor(), m.getIcono(), m.getOrden(), m.getActivo());
    }
}