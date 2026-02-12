package com.fundacionfomentodb.repository;

import com.fundacionfomentodb.entity.Convocatoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConvocatoriaRepository extends JpaRepository<Convocatoria, Integer> {
    Page<Convocatoria> findByActivaTrue(Pageable pageable);
    Page<Convocatoria> findByActiva(Boolean activa, Pageable pageable);
    Page<Convocatoria> findByTituloContainingIgnoreCase(String titulo, Pageable pageable);
}
