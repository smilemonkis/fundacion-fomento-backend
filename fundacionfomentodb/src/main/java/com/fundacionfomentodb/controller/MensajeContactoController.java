package com.fundacionfomentodb.controller;

import com.fundacionfomentodb.dto.*;
import com.fundacionfomentodb.service.MensajeContactoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/contacto")
@RequiredArgsConstructor
public class MensajeContactoController {

    private final MensajeContactoService service;

    // Público — enviar mensaje
    @PostMapping
    public ResponseEntity<MensajeContactoResponse> enviar(
            @Valid @RequestBody CreateMensajeContactoRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(req));
    }

    // Admin
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<MensajeContactoResponse>> listar(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(service.listar(PageRequest.of(page, size)));
    }

    @PutMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MensajeContactoResponse> actualizarEstado(
            @PathVariable Integer id, @RequestParam String estado) {
        return ResponseEntity.ok(service.actualizarEstado(id, estado));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/nuevos/count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> contarNuevos() {
        return ResponseEntity.ok(service.contarNuevos());
    }
}