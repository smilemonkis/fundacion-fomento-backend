package com.fundacionfomentodb.repository;

import com.fundacionfomentodb.entity.Noticia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NoticiaRepository extends JpaRepository<Noticia, Integer> {
    Page<Noticia> findByPublicado(Boolean publicado, Pageable pageable);

    @Query("SELECT n FROM Noticia n WHERE n.publicado = true AND n.id <> :excludeId ORDER BY n.fechaPublicacion DESC")
    Page<Noticia> findRelacionadas(Integer excludeId, Pageable pageable);
}