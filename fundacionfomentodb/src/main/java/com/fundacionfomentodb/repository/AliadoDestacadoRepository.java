package com.fundacionfomentodb.repository;

import com.fundacionfomentodb.entity.AliadoDestacado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AliadoDestacadoRepository extends JpaRepository<AliadoDestacado, Integer> {
    List<AliadoDestacado> findByActivoTrueOrderByOrdenAsc();
    List<AliadoDestacado> findAllByOrderByOrdenAsc();
}