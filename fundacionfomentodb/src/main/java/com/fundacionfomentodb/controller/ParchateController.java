package com.fundacionfomentodb.controller;

import com.fundacionfomentodb.dto.*;
import com.fundacionfomentodb.service.ParchateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/parchate")
@RequiredArgsConstructor
public class ParchateController {

    private final ParchateService parchateService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ParchateResponse> crear(@Valid @RequestBody CreateParchateRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(parchateService.crear(req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParchateResponse> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(parchateService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<Page<ParchateResponse>> listar(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String ubicacion,
            @RequestParam(required = false) Boolean activo,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "12") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(parchateService.listar(tipo, ubicacion, activo, pageable));
    }

    @GetMapping("/tipos")
    public ResponseEntity<List<String>> tipos() {
        return ResponseEntity.ok(parchateService.obtenerTipos());
    }

    @GetMapping("/ubicaciones")
    public ResponseEntity<List<String>> ubicaciones() {
        return ResponseEntity.ok(parchateService.obtenerUbicaciones());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ParchateResponse> actualizar(
            @PathVariable Integer id, @RequestBody UpdateParchateRequest req) {
        return ResponseEntity.ok(parchateService.actualizar(id, req));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        parchateService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}