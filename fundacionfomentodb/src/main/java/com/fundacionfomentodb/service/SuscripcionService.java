package com.fundacionfomentodb.service;

import com.fundacionfomentodb.dto.*;
import com.fundacionfomentodb.entity.Suscripcion;
import com.fundacionfomentodb.exception.BadRequestException;
import com.fundacionfomentodb.exception.ResourceNotFoundException;
import com.fundacionfomentodb.repository.SuscripcionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SuscripcionService {

    private final SuscripcionRepository suscripcionRepository;

    public SuscripcionResponse suscribir(CreateSuscripcionRequest req) {
        if (suscripcionRepository.existsByEmail(req.email())) {
            Suscripcion existente = suscripcionRepository.findByEmail(req.email()).get();
            if (!existente.getActivo()) {
                existente.setActivo(true);
                return toDto(suscripcionRepository.save(existente));
            }
            throw new BadRequestException("El correo ya está suscrito");
        }
        Suscripcion s = Suscripcion.builder().email(req.email()).build();
        return toDto(suscripcionRepository.save(s));
    }

    public void desuscribir(String email) {
        Suscripcion s = suscripcionRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Suscripción no encontrada"));
        s.setActivo(false);
        suscripcionRepository.save(s);
    }

    @Transactional(readOnly = true)
    public Page<SuscripcionResponse> listar(Boolean activo, Pageable pageable) {
        Page<Suscripcion> page = activo != null
                ? suscripcionRepository.findByActivo(activo, pageable)
                : suscripcionRepository.findAll(pageable);
        return page.map(this::toDto);
    }

    public void eliminar(Integer id) {
        suscripcionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Suscripción no encontrada"));
        suscripcionRepository.deleteById(id);
    }

    private SuscripcionResponse toDto(Suscripcion s) {
        return new SuscripcionResponse(
                s.getId(), s.getEmail(), s.getActivo(),
                s.getFechaSuscripcion().toString()
        );
    }
}