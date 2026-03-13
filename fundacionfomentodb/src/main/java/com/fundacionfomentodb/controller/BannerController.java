package com.fundacionfomentodb.controller;

import com.fundacionfomentodb.dto.*;
import com.fundacionfomentodb.service.BannerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/banners")
@RequiredArgsConstructor
public class BannerController {

    private final BannerService bannerService;

    // Público — solo activos y ordenados
    @GetMapping
    public ResponseEntity<List<BannerResponse>> listarActivos() {
        return ResponseEntity.ok(bannerService.listarActivos());
    }

    // Admin — todos
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BannerResponse>> listarTodos() {
        return ResponseEntity.ok(bannerService.listarTodos());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BannerResponse> crear(@Valid @RequestBody CreateBannerRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bannerService.crear(req));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BannerResponse> actualizar(
            @PathVariable Integer id, @RequestBody UpdateBannerRequest req) {
        return ResponseEntity.ok(bannerService.actualizar(id, req));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        bannerService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}