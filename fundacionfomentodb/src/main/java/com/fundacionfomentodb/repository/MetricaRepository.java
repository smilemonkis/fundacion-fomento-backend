package com.fundacionfomentodb.repository;

import com.fundacionfomentodb.entity.Metrica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MetricaRepository extends JpaRepository<Metrica, Integer> {
    List<Metrica> findByActivoTrueOrderByOrdenAsc();
    List<Metrica> findAllByOrderByOrdenAsc();
}