package com.fundacionfomentodb.controller;

import com.fundacionfomentodb.dto.*;
import com.fundacionfomentodb.service.NoticiaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/noticias")
@RequiredArgsConstructor
public class NoticiaController {

    private final NoticiaService noticiaService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NoticiaResponse> crear(@Valid @RequestBody CreateNoticiaRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(noticiaService.crear(req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoticiaResponse> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(noticiaService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<Page<NoticiaResponse>> listar(
            @RequestParam(required = false) Boolean publicado,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("fechaPublicacion").descending());
        return ResponseEntity.ok(noticiaService.listar(publicado, pageable));
    }

    @GetMapping("/{id}/relacionadas")
    public ResponseEntity<Page<NoticiaResponse>> relacionadas(
            @PathVariable Integer id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(noticiaService.relacionadas(id, pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NoticiaResponse> actualizar(
            @PathVariable Integer id, @RequestBody UpdateNoticiaRequest req) {
        return ResponseEntity.ok(noticiaService.actualizar(id, req));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        noticiaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}