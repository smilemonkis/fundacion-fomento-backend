package com.fundacionfomentodb.controller;

import com.fundacionfomentodb.dto.CreateProyectoRequest;
import com.fundacionfomentodb.dto.UpdateProyectoRequest;
import com.fundacionfomentodb.dto.ProyectoResponse;
import com.fundacionfomentodb.service.ProyectoService;
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
@RequestMapping("/api/v1/proyectos")
@RequiredArgsConstructor
public class ProyectoController {

    private final ProyectoService proyectoService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProyectoResponse> crearProyecto(@Valid @RequestBody CreateProyectoRequest request) {
        ProyectoResponse response = proyectoService.crearProyecto(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProyectoResponse> obtenerProyecto(@PathVariable Integer id) {
        ProyectoResponse response = proyectoService.obtenerProyectoPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<ProyectoResponse> obtenerProyectoPorCodigo(@PathVariable String codigo) {
        ProyectoResponse response = proyectoService.obtenerProyectoPorCodigo(codigo);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<ProyectoResponse>> listarProyectos(
            @RequestParam(required = false) Boolean activo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<ProyectoResponse> response = proyectoService.listarProyectos(activo, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/activos")
    public ResponseEntity<Page<ProyectoResponse>> listarProyectosActivos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<ProyectoResponse> response = proyectoService.listarProyectosActivos(pageable);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProyectoResponse> actualizarProyecto(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateProyectoRequest request) {
        ProyectoResponse response = proyectoService.actualizarProyecto(id, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/activar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProyectoResponse> activarProyecto(@PathVariable Integer id) {
        ProyectoResponse response = proyectoService.activarProyecto(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/desactivar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProyectoResponse> desactivarProyecto(@PathVariable Integer id) {
        ProyectoResponse response = proyectoService.desactivarProyecto(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarProyecto(@PathVariable Integer id) {
        proyectoService.eliminarProyecto(id);
        return ResponseEntity.noContent().build();
    }
}
