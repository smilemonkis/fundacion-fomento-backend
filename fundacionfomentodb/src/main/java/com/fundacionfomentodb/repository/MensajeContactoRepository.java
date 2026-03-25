package com.fundacionfomentodb.repository;

import com.fundacionfomentodb.entity.MensajeContacto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MensajeContactoRepository extends JpaRepository<MensajeContacto, Integer> {
    Page<MensajeContacto> findAllByOrderByCreatedAtDesc(Pageable pageable);
    long countByEstado(MensajeContacto.EstadoEnum estado);
}