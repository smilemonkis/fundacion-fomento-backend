package com.fundacionfomentodb.controller;

import com.fundacionfomentodb.dto.*;
import com.fundacionfomentodb.service.AliadoDestacadoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/aliados-destacados")
@RequiredArgsConstructor
public class AliadoDestacadoController {

    private final AliadoDestacadoService service;

    // Público — solo activos
    @GetMapping
    public ResponseEntity<List<AliadoDestacadoResponse>> listarActivos() {
        return ResponseEntity.ok(service.listarActivos());
    }

    // Admin — todos
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AliadoDestacadoResponse>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AliadoDestacadoResponse> crear(
            @Valid @RequestBody CreateAliadoDestacadoRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(req));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AliadoDestacadoResponse> actualizar(
            @PathVariable Integer id, @RequestBody UpdateAliadoDestacadoRequest req) {
        return ResponseEntity.ok(service.actualizar(id, req));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}