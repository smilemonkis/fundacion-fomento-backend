package com.fundacionfomentodb.controller;

import com.fundacionfomentodb.dto.CreateConvocatoriaRequest;
import com.fundacionfomentodb.dto.UpdateConvocatoriaRequest;
import com.fundacionfomentodb.dto.ConvocatoriaResponse;
import com.fundacionfomentodb.service.ConvocatoriaService;
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
@RequestMapping("/api/v1/convocatorias")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8081")
public class ConvocatoriaController {

    private final ConvocatoriaService convocatoriaService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ConvocatoriaResponse> crearConvocatoria(@Valid @RequestBody CreateConvocatoriaRequest request) {
        ConvocatoriaResponse response = convocatoriaService.crearConvocatoria(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConvocatoriaResponse> obtenerConvocatoria(@PathVariable Integer id) {
        ConvocatoriaResponse response = convocatoriaService.obtenerConvocatoriaPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<ConvocatoriaResponse>> listarConvocatorias(
            @RequestParam(required = false) Boolean activa,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<ConvocatoriaResponse> response = convocatoriaService.listarConvocatorias(activa, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/activas")
    public ResponseEntity<Page<ConvocatoriaResponse>> listarConvocatoriasActivas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<ConvocatoriaResponse> response = convocatoriaService.listarConvocatoriasActivas(pageable);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ConvocatoriaResponse> actualizarConvocatoria(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateConvocatoriaRequest request) {
        ConvocatoriaResponse response = convocatoriaService.actualizarConvocatoria(id, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/activar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ConvocatoriaResponse> activarConvocatoria(@PathVariable Integer id) {
        ConvocatoriaResponse response = convocatoriaService.activarConvocatoria(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/desactivar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ConvocatoriaResponse> desactivarConvocatoria(@PathVariable Integer id) {
        ConvocatoriaResponse response = convocatoriaService.desactivarConvocatoria(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarConvocatoria(@PathVariable Integer id) {
        convocatoriaService.eliminarConvocatoria(id);
        return ResponseEntity.noContent().build();
    }
}
