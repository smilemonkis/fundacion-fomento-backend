package com.fundacionfomentodb.service;

import com.fundacionfomentodb.dto.*;
import com.fundacionfomentodb.entity.Noticia;
import com.fundacionfomentodb.entity.NoticiaImagen;
import com.fundacionfomentodb.exception.ResourceNotFoundException;
import com.fundacionfomentodb.repository.NoticiaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticiaService {

    private final NoticiaRepository noticiaRepository;

    public NoticiaResponse crear(CreateNoticiaRequest req) {
        boolean publicado = Boolean.TRUE.equals(req.publicado());

        Noticia noticia = Noticia.builder()
                .titulo(req.titulo())
                .resumen(req.resumen())
                .contenido(req.contenido())
                .imagenUrl(req.imagenUrl())
                .autor(req.autor())
                .publicado(publicado)
                // Fecha auto al publicar, null si es borrador
                .fechaPublicacion(publicado ? LocalDate.now() : null)
                .build();

        agregarGaleria(noticia, req.galeria());
        return toDto(noticiaRepository.save(noticia));
    }

    @Transactional(readOnly = true)
    public NoticiaResponse obtenerPorId(Integer id) {
        return noticiaRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Noticia no encontrada"));
    }

    @Transactional(readOnly = true)
    public Page<NoticiaResponse> listar(Boolean publicado, Pageable pageable) {
        Page<Noticia> page = publicado != null
                ? noticiaRepository.findByPublicado(publicado, pageable)
                : noticiaRepository.findAll(pageable);
        return page.map(this::toDto);
    }

    @Transactional(readOnly = true)
    public Page<NoticiaResponse> relacionadas(Integer excludeId, Pageable pageable) {
        return noticiaRepository.findRelacionadas(excludeId, pageable).map(this::toDto);
    }

    public NoticiaResponse actualizar(Integer id, UpdateNoticiaRequest req) {
        Noticia n = noticiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Noticia no encontrada"));

        if (req.titulo()    != null) n.setTitulo(req.titulo());
        if (req.resumen()   != null) n.setResumen(req.resumen());
        if (req.contenido() != null) n.setContenido(req.contenido());
        if (req.imagenUrl() != null) n.setImagenUrl(req.imagenUrl());
        if (req.autor()     != null) n.setAutor(req.autor());

        // Lógica de publicación
        if (req.publicado() != null) {
            boolean yaEstabaPublicada = Boolean.TRUE.equals(n.getPublicado());
            boolean ahoraPublicada    = Boolean.TRUE.equals(req.publicado());

            n.setPublicado(ahoraPublicada);

            // Solo asignar fecha si se está publicando por primera vez
            if (ahoraPublicada && !yaEstabaPublicada) {
                n.setFechaPublicacion(LocalDate.now());
            }
            // Si se despublica, limpiar la fecha
            if (!ahoraPublicada) {
                n.setFechaPublicacion(null);
            }
        }

        if (req.galeria() != null) {
            n.getGaleria().clear();
            agregarGaleria(n, req.galeria());
        }

        return toDto(noticiaRepository.save(n));
    }

    public void eliminar(Integer id) {
        Noticia n = noticiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Noticia no encontrada"));
        noticiaRepository.delete(n);
    }

    private void agregarGaleria(Noticia noticia, List<ImagenRequest> lista) {
        if (lista == null || lista.isEmpty()) return;
        lista.forEach(img -> noticia.getGaleria().add(
                NoticiaImagen.builder()
                        .noticia(noticia)
                        .url(img.url())
                        .descripcion(img.descripcion())
                        .orden(img.orden() != null ? img.orden() : 0)
                        .build()
        ));
    }

    private NoticiaResponse toDto(Noticia n) {
        List<ImagenDto> galeria = n.getGaleria().stream()
                .sorted((a, b) -> a.getOrden().compareTo(b.getOrden()))
                .map(img -> new ImagenDto(img.getId(), img.getUrl(), img.getDescripcion(), img.getOrden()))
                .collect(Collectors.toList());

        return new NoticiaResponse(
                n.getId(), n.getTitulo(), n.getResumen(), n.getContenido(),
                n.getImagenUrl(), galeria, n.getAutor(), n.getPublicado(),
                n.getFechaPublicacion() != null ? n.getFechaPublicacion().toString() : null,
                n.getCreatedAt().toString(), n.getUpdatedAt().toString()
        );
    }
}