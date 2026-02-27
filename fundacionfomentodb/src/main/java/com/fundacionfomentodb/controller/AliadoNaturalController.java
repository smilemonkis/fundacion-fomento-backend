package com.fundacionfomentodb.controller;

import com.fundacionfomentodb.dto.*;
import com.fundacionfomentodb.service.AliadoNaturalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/aliados-naturales")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8081")
public class AliadoNaturalController {
    
    private final AliadoNaturalService aliadoNaturalService;
    
    @PostMapping
    public ResponseEntity<AliadoNaturalResponse> crearAliadoNatural(
            @Valid @RequestBody CreateAliadoNaturalRequest request) {
        AliadoNaturalResponse response = aliadoNaturalService.crearAliadoNatural(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<AliadoNaturalResponse> obtenerAliadoNatural(@PathVariable Integer id) {
        AliadoNaturalResponse response = aliadoNaturalService.obtenerAliadoPorId(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/documento/{documento}")
    public ResponseEntity<AliadoNaturalResponse> obtenerAliadoPorDocumento(
            @PathVariable String documento) {
        AliadoNaturalResponse response = aliadoNaturalService.obtenerAliadoPorDocumento(documento);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    public ResponseEntity<Page<AliadoNaturalResponse>> listarAliadosNaturales(
            @RequestParam(required = false) String documento,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize) {
        
        SearchAliadoNaturalRequest searchRequest = new SearchAliadoNaturalRequest(
            documento, nombre, email, pageNumber, pageSize
        );
        Page<AliadoNaturalResponse> response = aliadoNaturalService.listarAliados(searchRequest);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<AliadoNaturalResponse> actualizarAliadoNatural(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateAliadoNaturalRequest request) {
        AliadoNaturalResponse response = aliadoNaturalService.actualizarAliadoNatural(id, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAliadoNatural(@PathVariable Integer id) {
        aliadoNaturalService.eliminarAliadoNatural(id);
        return ResponseEntity.noContent().build();
    }
}
