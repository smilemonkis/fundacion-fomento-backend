package com.fundacionfomentodb.service;

import com.fundacionfomentodb.dto.*;
import com.fundacionfomentodb.entity.Banner;
import com.fundacionfomentodb.exception.ResourceNotFoundException;
import com.fundacionfomentodb.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BannerService {

    private final BannerRepository bannerRepository;

    public BannerResponse crear(CreateBannerRequest req) {
        Banner b = Banner.builder()
                .titulo(req.titulo())
                .subtitulo(req.subtitulo())
                .imagenUrl(req.imagenUrl())
                .ctaTexto(req.ctaTexto())
                .ctaLink(req.ctaLink())
                .ctaColor(req.ctaColor() != null ? req.ctaColor() : "default")
                .orden(req.orden() != null ? req.orden() : 0)
                .build();
        return toDto(bannerRepository.save(b));
    }

    @Transactional(readOnly = true)
    public List<BannerResponse> listarActivos() {
        return bannerRepository.findByActivoTrueOrderByOrdenAsc()
                .stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<BannerResponse> listarTodos() {
        return bannerRepository.findAllByOrderByOrdenAsc()
                .stream().map(this::toDto).toList();
    }

    public BannerResponse actualizar(Integer id, UpdateBannerRequest req) {
        Banner b = bannerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Banner no encontrado"));
        if (req.titulo()    != null) b.setTitulo(req.titulo());
        if (req.subtitulo() != null) b.setSubtitulo(req.subtitulo());
        if (req.imagenUrl() != null) b.setImagenUrl(req.imagenUrl());
        if (req.ctaTexto()  != null) b.setCtaTexto(req.ctaTexto());
        if (req.ctaLink()   != null) b.setCtaLink(req.ctaLink());
        if (req.ctaColor()  != null) b.setCtaColor(req.ctaColor());
        if (req.orden()     != null) b.setOrden(req.orden());
        if (req.activo()    != null) b.setActivo(req.activo());
        return toDto(bannerRepository.save(b));
    }

    public void eliminar(Integer id) {
        bannerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Banner no encontrado"));
        bannerRepository.deleteById(id);
    }

    private BannerResponse toDto(Banner b) {
        return new BannerResponse(
                b.getId(), b.getTitulo(), b.getSubtitulo(), b.getImagenUrl(),
                b.getCtaTexto(), b.getCtaLink(), b.getCtaColor(), b.getOrden(),
                b.getActivo(), b.getCreatedAt().toString(), b.getUpdatedAt().toString()
        );
    }
}