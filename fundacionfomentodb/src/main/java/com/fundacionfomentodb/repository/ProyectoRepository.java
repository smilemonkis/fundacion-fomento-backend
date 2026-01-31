package com.fundacionfomentodb.repository;

import com.fundacionfomentodb.entity.Proyecto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Integer> {
    Optional<Proyecto> findByCodigo(String codigo);
    Page<Proyecto> findByActivoTrue(Pageable pageable);
    Page<Proyecto> findByActivo(Boolean activo, Pageable pageable);
    Page<Proyecto> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);
}
