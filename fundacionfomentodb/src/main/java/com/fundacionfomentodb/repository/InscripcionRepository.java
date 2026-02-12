package com.fundacionfomentodb.repository;

import com.fundacionfomentodb.entity.Inscripcion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Integer> {
    Optional<Inscripcion> findByUsuarioIdAndConvocatoriaId(Integer usuarioId, Integer convocatoriaId);
    boolean existsByUsuarioIdAndConvocatoriaId(Integer usuarioId, Integer convocatoriaId);
    Page<Inscripcion> findByUsuarioId(Integer usuarioId, Pageable pageable);
    Page<Inscripcion> findByConvocatoriaId(Integer convocatoriaId, Pageable pageable);
    Page<Inscripcion> findByEstado(Inscripcion.EstadoEnum estado, Pageable pageable);
}
