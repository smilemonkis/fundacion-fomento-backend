package com.fundacionfomentodb.controller;

import com.fundacionfomentodb.dto.CreateDonacionRequest;
import com.fundacionfomentodb.dto.UpdateDonacionRequest;
import com.fundacionfomentodb.dto.DonacionResponse;
import com.fundacionfomentodb.service.DonacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/donaciones")
@RequiredArgsConstructor
public class DonacionController {

    private final DonacionService donacionService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ALIADO_NAT', 'ALIADO_JUR')")
    public ResponseEntity<DonacionResponse> crearDonacion(@Valid @RequestBody CreateDonacionRequest request) {
        DonacionResponse response = donacionService.crearDonacion(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ALIADO_NAT', 'ALIADO_JUR')")
    public ResponseEntity<DonacionResponse> obtenerDonacion(@PathVariable Integer id) {
        DonacionResponse response = donacionService.obtenerDonacionPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<DonacionResponse>> listarDonaciones(
            @RequestParam(required = false) Integer usuarioId,
            @RequestParam(required = false) Integer proyectoId,
            @RequestParam(required = false) String estado,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<DonacionResponse> response = donacionService.listarDonaciones(usuarioId, proyectoId, estado, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/usuario/{usuarioId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ALIADO_NAT', 'ALIADO_JUR')")
    public ResponseEntity<Page<DonacionResponse>> listarDonacionesPorUsuario(
            @PathVariable Integer usuarioId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<DonacionResponse> response = donacionService.listarDonacionesPorUsuario(usuarioId, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/proyecto/{proyectoId}")
    public ResponseEntity<Page<DonacionResponse>> listarDonacionesPorProyecto(
            @PathVariable Integer proyectoId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<DonacionResponse> response = donacionService.listarDonacionesPorProyecto(proyectoId, pageable);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DonacionResponse> actualizarDonacion(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateDonacionRequest request) {
        DonacionResponse response = donacionService.actualizarDonacion(id, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/aprobar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DonacionResponse> aprobarDonacion(@PathVariable Integer id) {
        DonacionResponse response = donacionService.aprobarDonacion(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/rechazar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DonacionResponse> rechazarDonacion(@PathVariable Integer id) {
        DonacionResponse response = donacionService.rechazarDonacion(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/cancelar")
    @PreAuthorize("hasAnyRole('ADMIN', 'ALIADO_NAT', 'ALIADO_JUR')")
    public ResponseEntity<DonacionResponse> cancelarDonacion(@PathVariable Integer id) {
        DonacionResponse response = donacionService.cancelarDonacion(id);
        return ResponseEntity.ok(response);
    }
}
