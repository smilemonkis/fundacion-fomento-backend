package com.fundacionfomentodb.service;

import com.fundacionfomentodb.dto.*;
import com.fundacionfomentodb.entity.MensajeContacto;
import com.fundacionfomentodb.exception.ResourceNotFoundException;
import com.fundacionfomentodb.repository.MensajeContactoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MensajeContactoService {

    private final MensajeContactoRepository repo;

    // Público — guardar mensaje
    public MensajeContactoResponse crear(CreateMensajeContactoRequest req) {
        MensajeContacto m = MensajeContacto.builder()
                .nombre(req.nombre())
                .telefono(req.telefono())
                .correo(req.correo())
                .mensaje(req.mensaje())
                .build();
        return toDto(repo.save(m));
    }

    // Admin — listar todos
    @Transactional(readOnly = true)
    public Page<MensajeContactoResponse> listar(Pageable pageable) {
        return repo.findAllByOrderByCreatedAtDesc(pageable).map(this::toDto);
    }

    // Admin — marcar estado
    public MensajeContactoResponse actualizarEstado(Integer id, String estado) {
        MensajeContacto m = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mensaje no encontrado"));
        m.setEstado(MensajeContacto.EstadoEnum.valueOf(estado));
        return toDto(repo.save(m));
    }

    // Admin — eliminar
    public void eliminar(Integer id) {
        repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Mensaje no encontrado"));
        repo.deleteById(id);
    }

    // Admin — contar nuevos
    @Transactional(readOnly = true)
    public long contarNuevos() {
        return repo.countByEstado(MensajeContacto.EstadoEnum.NUEVO);
    }

    private MensajeContactoResponse toDto(MensajeContacto m) {
        return new MensajeContactoResponse(
                m.getId(), m.getNombre(), m.getTelefono(), m.getCorreo(),
                m.getMensaje(), m.getEstado().name(),
                m.getCreatedAt() != null ? m.getCreatedAt().toString() : null
        );
    }
}