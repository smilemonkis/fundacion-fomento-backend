package com.fundacionfomentodb.repository;

import com.fundacionfomentodb.entity.Suscripcion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SuscripcionRepository extends JpaRepository<Suscripcion, Integer> {
    Optional<Suscripcion> findByEmail(String email);
    boolean existsByEmail(String email);
    Page<Suscripcion> findByActivo(Boolean activo, Pageable pageable);
}