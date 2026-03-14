package com.fundacionfomentodb.controller;

import com.fundacionfomentodb.dto.*;
import com.fundacionfomentodb.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:8082"})
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioResponse> crearUsuario(
            @Valid @RequestBody CreateUsuarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuarioService.crearUsuario(request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponse> obtenerUsuario(@PathVariable Integer id) {
        return ResponseEntity.ok(usuarioService.obtenerUsuarioPorId(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UsuarioResponse>> listarUsuarios(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String rol,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(usuarioService.listarUsuarios(email, rol, pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponse> actualizarUsuario(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateUsuarioRequest request) {
        return ResponseEntity.ok(usuarioService.actualizarUsuario(id, request));
    }

    // ── NUEVO: cualquier usuario autenticado puede cambiar su propia contraseña ──
    @PutMapping("/{id}/cambiar-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> cambiarPassword(
            @PathVariable Integer id,
            @Valid @RequestBody CambiarPasswordRequest request) {
        usuarioService.cambiarPassword(id, request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/activar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponse> activarUsuario(@PathVariable Integer id) {
        return ResponseEntity.ok(usuarioService.activarUsuario(id));
    }

    @PutMapping("/{id}/desactivar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponse> desactivarUsuario(@PathVariable Integer id) {
        return ResponseEntity.ok(usuarioService.desactivarUsuario(id));
    }
}