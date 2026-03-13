package com.fundacionfomentodb.controller;

import com.fundacionfomentodb.dto.*;
import com.fundacionfomentodb.service.SuscripcionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/suscripciones")
@RequiredArgsConstructor
public class SuscripcionController {

    private final SuscripcionService suscripcionService;

    @PostMapping
    public ResponseEntity<SuscripcionResponse> suscribir(@Valid @RequestBody CreateSuscripcionRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(suscripcionService.suscribir(req));
    }

    @DeleteMapping("/email/{email}")
    public ResponseEntity<Void> desuscribir(@PathVariable String email) {
        suscripcionService.desuscribir(email);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<SuscripcionResponse>> listar(
            @RequestParam(required = false) Boolean activo,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "50") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("fechaSuscripcion").descending());
        return ResponseEntity.ok(suscripcionService.listar(activo, pageable));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        suscripcionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}