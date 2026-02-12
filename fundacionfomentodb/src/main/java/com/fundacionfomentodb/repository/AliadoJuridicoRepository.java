package com.fundacionfomentodb.repository;

import com.fundacionfomentodb.entity.AliadoJuridico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AliadoJuridicoRepository extends JpaRepository<AliadoJuridico, Integer> {
    Optional<AliadoJuridico> findByNit(String nit);

    Optional<AliadoJuridico> findByUsuarioId(Integer usuarioId);

    Page<AliadoJuridico> findByRazonSocialContainingIgnoreCase(String razonSocial, Pageable pageable);

    Page<AliadoJuridico> findByNit(String nit, Pageable pageable);
}
