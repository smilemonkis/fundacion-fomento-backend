package com.fundacionfomentodb.controller;

import com.fundacionfomentodb.dto.*;
import com.fundacionfomentodb.service.AliadoJuridicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/aliados-juridicos")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8081")
public class AliadoJuridicoController {
    
    private final AliadoJuridicoService aliadoJuridicoService;
    
    @PostMapping
    public ResponseEntity<AliadoJuridicoResponse> crearAliadoJuridico(
            @Valid @RequestBody CreateAliadoJuridicoRequest request) {
        AliadoJuridicoResponse response = aliadoJuridicoService.crearAliadoJuridico(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<AliadoJuridicoResponse> obtenerAliadoJuridico(@PathVariable Integer id) {
        AliadoJuridicoResponse response = aliadoJuridicoService.obtenerAliadoPorId(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/nit/{nit}")
    public ResponseEntity<AliadoJuridicoResponse> obtenerAliadoPorNit(
            @PathVariable String nit) {
        AliadoJuridicoResponse response = aliadoJuridicoService.obtenerAliadoPorNit(nit);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    public ResponseEntity<Page<AliadoJuridicoResponse>> listarAliadosJuridicos(
            @RequestParam(required = false) String nit,
            @RequestParam(required = false) String razonSocial,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize) {
        
        SearchAliadoJuridicoRequest searchRequest = new SearchAliadoJuridicoRequest(
            nit, razonSocial, email, pageNumber, pageSize
        );
        Page<AliadoJuridicoResponse> response = aliadoJuridicoService.listarAliados(searchRequest);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<AliadoJuridicoResponse> actualizarAliadoJuridico(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateAliadoJuridicoRequest request) {
        AliadoJuridicoResponse response = aliadoJuridicoService.actualizarAliadoJuridico(id, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAliadoJuridico(@PathVariable Integer id) {
        aliadoJuridicoService.eliminarAliadoJuridico(id);
        return ResponseEntity.noContent().build();
    }
}
