package com.fundacionfomentodb.repository;

import com.fundacionfomentodb.entity.Donacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DonacionRepository extends JpaRepository<Donacion, Integer> {
    Page<Donacion> findByUsuarioId(Integer usuarioId, Pageable pageable);

    Page<Donacion> findByEstado(Donacion.EstadoEnum estado, Pageable pageable);

    Page<Donacion> findByProyectoId(Integer proyectoId, Pageable pageable);
}
