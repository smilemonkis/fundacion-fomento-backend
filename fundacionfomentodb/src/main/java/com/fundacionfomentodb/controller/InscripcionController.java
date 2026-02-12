package com.fundacionfomentodb.controller;

import com.fundacionfomentodb.dto.CreateInscripcionRequest;
import com.fundacionfomentodb.dto.UpdateInscripcionRequest;
import com.fundacionfomentodb.dto.InscripcionResponse;
import com.fundacionfomentodb.service.InscripcionService;
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
@RequestMapping("/api/v1/inscripciones")
@RequiredArgsConstructor
public class InscripcionController {

    private final InscripcionService inscripcionService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ESTUDIANTE', 'ASPIRANTE')")
    public ResponseEntity<InscripcionResponse> crearInscripcion(@Valid @RequestBody CreateInscripcionRequest request) {
        InscripcionResponse response = inscripcionService.crearInscripcion(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ESTUDIANTE', 'ASPIRANTE')")
    public ResponseEntity<InscripcionResponse> obtenerInscripcion(@PathVariable Integer id) {
        InscripcionResponse response = inscripcionService.obtenerInscripcionPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<InscripcionResponse>> listarInscripciones(
            @RequestParam(required = false) Integer usuarioId,
            @RequestParam(required = false) Integer convocatoriaId,
            @RequestParam(required = false) String estado,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<InscripcionResponse> response = inscripcionService.listarInscripciones(usuarioId, convocatoriaId, estado, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/usuario/{usuarioId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ESTUDIANTE', 'ASPIRANTE')")
    public ResponseEntity<Page<InscripcionResponse>> listarInscripcionesPorUsuario(
            @PathVariable Integer usuarioId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<InscripcionResponse> response = inscripcionService.listarInscripcionesPorUsuario(usuarioId, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/convocatoria/{convocatoriaId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<InscripcionResponse>> listarInscripcionesPorConvocatoria(
            @PathVariable Integer convocatoriaId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<InscripcionResponse> response = inscripcionService.listarInscripcionesPorConvocatoria(convocatoriaId, pageable);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InscripcionResponse> actualizarInscripcion(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateInscripcionRequest request) {
        InscripcionResponse response = inscripcionService.actualizarInscripcion(id, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/aprobar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InscripcionResponse> aprobarInscripcion(@PathVariable Integer id) {
        InscripcionResponse response = inscripcionService.aprobarInscripcion(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/rechazar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InscripcionResponse> rechazarInscripcion(@PathVariable Integer id) {
        InscripcionResponse response = inscripcionService.rechazarInscripcion(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ESTUDIANTE', 'ASPIRANTE')")
    public ResponseEntity<Void> eliminarInscripcion(@PathVariable Integer id) {
        inscripcionService.eliminarInscripcion(id);
        return ResponseEntity.noContent().build();
    }
}
