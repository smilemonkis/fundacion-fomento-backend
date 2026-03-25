package com.fundacionfomentodb.repository;

import com.fundacionfomentodb.entity.Parchate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ParchateRepository extends JpaRepository<Parchate, Integer> {
    Page<Parchate> findByActivo(Boolean activo, Pageable pageable);
    Page<Parchate> findByTipoIgnoreCaseAndActivo(String tipo, Boolean activo, Pageable pageable);
    Page<Parchate> findByUbicacionIgnoreCaseAndActivo(String ubicacion, Boolean activo, Pageable pageable);
    Page<Parchate> findByTipoIgnoreCaseAndUbicacionIgnoreCaseAndActivo(String tipo, String ubicacion, Boolean activo, Pageable pageable);

    // Para filtrar solo por tipo sin importar activo
    Page<Parchate> findByTipoIgnoreCase(String tipo, Pageable pageable);

    // Para filtrar solo por ubicacion sin importar activo
    Page<Parchate> findByUbicacionIgnoreCase(String ubicacion, Pageable pageable);

    @Query("SELECT DISTINCT p.tipo FROM Parchate p WHERE p.activo = true ORDER BY p.tipo")
    List<String> findTiposActivos();

    @Query("SELECT DISTINCT p.ubicacion FROM Parchate p WHERE p.activo = true ORDER BY p.ubicacion")
    List<String> findUbicacionesActivas();

    // Aliases usados por el service
    default List<String> findDistinctTipos() { return findTiposActivos(); }
    default List<String> findDistinctUbicaciones() { return findUbicacionesActivas(); }
}