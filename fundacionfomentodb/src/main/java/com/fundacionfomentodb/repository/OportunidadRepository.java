package com.fundacionfomentodb.repository;

import com.fundacionfomentodb.entity.Oportunidad;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OportunidadRepository extends JpaRepository<Oportunidad, Integer> {
    Page<Oportunidad> findByActivo(Boolean activo, Pageable pageable);
    Page<Oportunidad> findByTipo(Oportunidad.TipoEnum tipo, Pageable pageable);
    Page<Oportunidad> findByTipoAndActivo(Oportunidad.TipoEnum tipo, Boolean activo, Pageable pageable);
}