package com.fundacionfomentodb.service;

import com.fundacionfomentodb.dto.*;
import com.fundacionfomentodb.entity.Proyecto;
import com.fundacionfomentodb.exception.BadRequestException;
import com.fundacionfomentodb.exception.ResourceNotFoundException;
import com.fundacionfomentodb.repository.ProyectoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProyectoService {

    private final ProyectoRepository proyectoRepository;

    public ProyectoResponse crearProyecto(CreateProyectoRequest req) {
        if (proyectoRepository.findByCodigo(req.codigo()).isPresent()) {
            throw new BadRequestException("El código " + req.codigo() + " ya está registrado");
        }
        validarFechas(req.fechaInicio(), req.fechaFin());
        Proyecto p = Proyecto.builder()
                .codigo(req.codigo())
                .nombre(req.nombre())
                .descripcion(req.descripcion())
                .fechaInicio(req.fechaInicio())
                .fechaFin(req.fechaFin())
                .activo(true)
                .estado(parseEstado(req.estado(), Proyecto.EstadoEnum.ABIERTO))
                .imagenUrl(req.imagenUrl())
                .beneficiarios(req.beneficiarios())
                .presupuesto(req.presupuesto())
                .progreso(req.progreso() != null ? req.progreso() : 0)
                .build();
        return toDto(proyectoRepository.save(p));
    }

    @Transactional(readOnly = true)
    public ProyectoResponse obtenerProyectoPorId(Integer id) {
        return proyectoRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado"));
    }

    @Transactional(readOnly = true)
    public ProyectoResponse obtenerProyectoPorCodigo(String codigo) {
        return proyectoRepository.findByCodigo(codigo)
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado"));
    }

    @Transactional(readOnly = true)
    public Page<ProyectoResponse> listarProyectos(Boolean activo, Pageable pageable) {
        Page<Proyecto> resultado = activo != null
                ? proyectoRepository.findByActivo(activo, pageable)
                : proyectoRepository.findAll(pageable);
        return resultado.map(this::toDto);
    }

    @Transactional(readOnly = true)
    public Page<ProyectoResponse> listarProyectosActivos(Pageable pageable) {
        return proyectoRepository.findByActivo(true, pageable).map(this::toDto);
    }

    public ProyectoResponse actualizarProyecto(Integer id, UpdateProyectoRequest req) {
        Proyecto p = proyectoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado"));

        if (req.codigo() != null && !req.codigo().isBlank() && !p.getCodigo().equals(req.codigo())) {
            if (proyectoRepository.findByCodigo(req.codigo()).isPresent())
                throw new BadRequestException("El código ya está en uso");
            p.setCodigo(req.codigo());
        }
        if (req.nombre()       != null) p.setNombre(req.nombre());
        if (req.descripcion()  != null) p.setDescripcion(req.descripcion());
        if (req.imagenUrl()    != null) p.setImagenUrl(req.imagenUrl());
        if (req.beneficiarios()!= null) p.setBeneficiarios(req.beneficiarios());
        if (req.presupuesto()  != null) p.setPresupuesto(req.presupuesto());
        if (req.progreso()     != null) p.setProgreso(req.progreso());
        if (req.estado()       != null) p.setEstado(Proyecto.EstadoEnum.valueOf(req.estado()));

        var newInicio = req.fechaInicio() != null ? req.fechaInicio() : p.getFechaInicio();
        var newFin    = req.fechaFin()    != null ? req.fechaFin()    : p.getFechaFin();
        validarFechas(newInicio, newFin);
        p.setFechaInicio(newInicio);
        p.setFechaFin(newFin);

        return toDto(proyectoRepository.save(p));
    }

    public ProyectoResponse activarProyecto(Integer id) {
        Proyecto p = proyectoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado"));
        p.setActivo(true);
        return toDto(proyectoRepository.save(p));
    }

    public ProyectoResponse desactivarProyecto(Integer id) {
        Proyecto p = proyectoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado"));
        p.setActivo(false);
        return toDto(proyectoRepository.save(p));
    }

    public void eliminarProyecto(Integer id) {
        Proyecto p = proyectoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado"));
        proyectoRepository.delete(p);
    }

    private void validarFechas(java.time.LocalDate inicio, java.time.LocalDate fin) {
        if (inicio != null && fin != null && !fin.isAfter(inicio)) {
            throw new BadRequestException("La fecha de fin debe ser posterior a la fecha de inicio");
        }
    }

    private Proyecto.EstadoEnum parseEstado(String estado, Proyecto.EstadoEnum defecto) {
        if (estado == null) return defecto;
        try { return Proyecto.EstadoEnum.valueOf(estado); }
        catch (Exception e) { return defecto; }
    }

    private ProyectoResponse toDto(Proyecto p) {
        return new ProyectoResponse(
                p.getId(), p.getCodigo(), p.getNombre(), p.getDescripcion(),
                p.getFechaInicio(), p.getFechaFin(), p.getActivo(),
                p.getImagenUrl(), p.getBeneficiarios(), p.getPresupuesto(),
                p.getProgreso(), p.getEstado().name(),
                p.getCreatedAt(), p.getUpdatedAt()
        );
    }
}