package com.fundacionfomentodb.controller;

import com.fundacionfomentodb.dto.*;
import com.fundacionfomentodb.service.DonacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/donaciones")
@RequiredArgsConstructor
public class DonacionController {

    private final DonacionService donacionService;

    @PostMapping("/aliado")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<DonacionResponse> donarAliado(
            @RequestBody CreateDonacionAliadoRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(donacionService.crearDesdeAliado(req));
    }

    // Público — cualquiera puede donar sin cuenta
    @PostMapping("/publica")
    public ResponseEntity<DonacionResponse> donarPublico(
            @Valid @RequestBody CreateDonacionPublicaRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(donacionService.crearPublica(req));
    }

    // Admin
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<DonacionResponse>> listar(
            @RequestParam(defaultValue = "0")   int page,
            @RequestParam(defaultValue = "200") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("fecha").descending());
        return ResponseEntity.ok(donacionService.listarTodas(pageable));
    }

    @GetMapping("/usuario/{usuarioId}")
    @PreAuthorize("isAuthenticated()") // Solo usuarios logueados
    public ResponseEntity<Page<DonacionResponse>> listarPorUsuario(
            @PathVariable Integer usuarioId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("fecha").descending());
        return ResponseEntity.ok(donacionService.listarPorUsuario(usuarioId, pageable));
    }

    @PutMapping("/{id}/aprobar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DonacionResponse> aprobar(@PathVariable Integer id) {
        return ResponseEntity.ok(donacionService.confirmar(id));
    }

    @PutMapping("/{id}/rechazar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DonacionResponse> rechazar(@PathVariable Integer id) {
        return ResponseEntity.ok(donacionService.rechazar(id));
    }

    @PutMapping("/{id}/cancelar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DonacionResponse> cancelar(@PathVariable Integer id) {
        return ResponseEntity.ok(donacionService.cancelar(id));
    }
}