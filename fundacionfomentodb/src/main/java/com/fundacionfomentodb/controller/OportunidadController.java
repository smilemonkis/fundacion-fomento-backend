package com.fundacionfomentodb.controller;

import com.fundacionfomentodb.dto.*;
import com.fundacionfomentodb.service.OportunidadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/oportunidades")
@RequiredArgsConstructor
public class OportunidadController {

    private final OportunidadService oportunidadService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OportunidadResponse> crear(@Valid @RequestBody CreateOportunidadRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(oportunidadService.crear(req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OportunidadResponse> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(oportunidadService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<Page<OportunidadResponse>> listar(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) Boolean activo,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(oportunidadService.listar(tipo, activo, pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OportunidadResponse> actualizar(
            @PathVariable Integer id, @RequestBody UpdateOportunidadRequest req) {
        return ResponseEntity.ok(oportunidadService.actualizar(id, req));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        oportunidadService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}