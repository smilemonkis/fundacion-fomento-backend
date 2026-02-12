package com.fundacionfomentodb.service;

import com.fundacionfomentodb.dto.CreateProyectoRequest;
import com.fundacionfomentodb.dto.UpdateProyectoRequest;
import com.fundacionfomentodb.dto.ProyectoResponse;
import com.fundacionfomentodb.entity.Proyecto;
import com.fundacionfomentodb.repository.ProyectoRepository;
import com.fundacionfomentodb.exception.BadRequestException;
import com.fundacionfomentodb.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProyectoService {

    private final ProyectoRepository proyectoRepository;

    public ProyectoResponse crearProyecto(CreateProyectoRequest request) {
        if (proyectoRepository.findByCodigo(request.codigo()).isPresent()) {
            throw new BadRequestException("El código " + request.codigo() + " ya está registrado");
        }

        Proyecto proyecto = Proyecto.builder()
                .codigo(request.codigo())
                .nombre(request.nombre())
                .descripcion(request.descripcion())
                .fechaInicio(request.fechaInicio())
                .fechaFin(request.fechaFin())
                .activo(true)
                .build();

        Proyecto proyectoGuardado = proyectoRepository.save(proyecto);
        return toResponseDto(proyectoGuardado);
    }

    @Transactional(readOnly = true)
    public ProyectoResponse obtenerProyectoPorId(Integer id) {
        return proyectoRepository.findById(id)
                .map(this::toResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado"));
    }

    @Transactional(readOnly = true)
    public ProyectoResponse obtenerProyectoPorCodigo(String codigo) {
        return proyectoRepository.findByCodigo(codigo)
                .map(this::toResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto con código " + codigo + " no encontrado"));
    }

    @Transactional(readOnly = true)
    public Page<ProyectoResponse> listarProyectos(Boolean activo, Pageable pageable) {
        Page<Proyecto> resultado;
        
        if (activo != null) {
            resultado = proyectoRepository.findByActivo(activo, pageable);
        } else {
            resultado = proyectoRepository.findAll(pageable);
        }
        
        return resultado.map(this::toResponseDto);
    }

    @Transactional(readOnly = true)
    public Page<ProyectoResponse> listarProyectosActivos(Pageable pageable) {
        return proyectoRepository.findByActivo(true, pageable)
                .map(this::toResponseDto);
    }

    public ProyectoResponse actualizarProyecto(Integer id, UpdateProyectoRequest request) {
        Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado"));

        if (request.codigo() != null && !request.codigo().isBlank() 
            && !proyecto.getCodigo().equals(request.codigo())) {
            
            if (proyectoRepository.findByCodigo(request.codigo()).isPresent()) {
                throw new BadRequestException("El código ya está en uso");
            }
            proyecto.setCodigo(request.codigo());
        }

        if (request.nombre() != null && !request.nombre().isBlank()) {
            proyecto.setNombre(request.nombre());
        }

        if (request.descripcion() != null && !request.descripcion().isBlank()) {
            proyecto.setDescripcion(request.descripcion());
        }

        if (request.fechaInicio() != null) {
            proyecto.setFechaInicio(request.fechaInicio());
        }

        if (request.fechaFin() != null) {
            proyecto.setFechaFin(request.fechaFin());
        }

        Proyecto proyectoActualizado = proyectoRepository.save(proyecto);
        return toResponseDto(proyectoActualizado);
    }

    public ProyectoResponse activarProyecto(Integer id) {
        Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado"));
        
        proyecto.setActivo(true);
        Proyecto proyectoActualizado = proyectoRepository.save(proyecto);
        return toResponseDto(proyectoActualizado);
    }

    public ProyectoResponse desactivarProyecto(Integer id) {
        Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado"));
        
        proyecto.setActivo(false);
        Proyecto proyectoActualizado = proyectoRepository.save(proyecto);
        return toResponseDto(proyectoActualizado);
    }

    public void eliminarProyecto(Integer id) {
        Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado"));
        
        proyectoRepository.delete(proyecto);
    }

    private ProyectoResponse toResponseDto(Proyecto proyecto) {
        return new ProyectoResponse(
                proyecto.getId(),
                proyecto.getCodigo(),
                proyecto.getNombre(),
                proyecto.getDescripcion(),
                proyecto.getFechaInicio(),
                proyecto.getFechaFin(),
                proyecto.getActivo(),
                proyecto.getCreatedAt(),
                proyecto.getUpdatedAt()
        );
    }
}
