package com.fundacionfomentodb.repository;

import com.fundacionfomentodb.entity.AliadoNatural;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AliadoNaturalRepository extends JpaRepository<AliadoNatural, Integer> {
    Optional<AliadoNatural> findByDocumento(String documento);

    Optional<AliadoNatural> findByUsuarioId(Integer usuarioId);

    Page<AliadoNatural> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);

    Page<AliadoNatural> findByDocumento(String documento, Pageable pageable);
}
