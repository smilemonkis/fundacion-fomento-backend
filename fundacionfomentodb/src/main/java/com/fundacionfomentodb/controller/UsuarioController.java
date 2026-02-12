package com.fundacionfomentodb.controller;

import com.fundacionfomentodb.dto.CreateUsuarioRequest;
import com.fundacionfomentodb.dto.UpdateUsuarioRequest;
import com.fundacionfomentodb.dto.UsuarioResponse;
import com.fundacionfomentodb.service.UsuarioService;
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
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponse> crearUsuario(@Valid @RequestBody CreateUsuarioRequest request) {
        UsuarioResponse response = usuarioService.crearUsuario(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponse> obtenerUsuario(@PathVariable Integer id) {
        UsuarioResponse response = usuarioService.obtenerUsuarioPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UsuarioResponse>> listarUsuarios(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String rol,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<UsuarioResponse> response = usuarioService.listarUsuarios(email, rol, pageable);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponse> actualizarUsuario(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateUsuarioRequest request) {
        UsuarioResponse response = usuarioService.actualizarUsuario(id, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/activar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponse> activarUsuario(@PathVariable Integer id) {
        UsuarioResponse response = usuarioService.activarUsuario(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/desactivar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponse> desactivarUsuario(@PathVariable Integer id) {
        UsuarioResponse response = usuarioService.desactivarUsuario(id);
        return ResponseEntity.ok(response);
    }
}
