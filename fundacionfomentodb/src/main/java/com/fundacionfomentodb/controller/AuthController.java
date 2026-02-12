package com.fundacionfomentodb.controller;

import com.fundacionfomentodb.config.JwtService;
import com.fundacionfomentodb.dto.LoginRequest;
import com.fundacionfomentodb.dto.LoginResponse;
import com.fundacionfomentodb.entity.Usuario;
import com.fundacionfomentodb.repository.UsuarioRepository;
import com.fundacionfomentodb.exception.UnauthorizedException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(userDetails);

            Usuario usuario = usuarioRepository.findByEmail(request.email())
                    .orElseThrow(() -> new UnauthorizedException("Usuario no encontrado"));

            LoginResponse response = new LoginResponse(
                    token,
                    usuario.getId(),
                    usuario.getEmail(),
                    usuario.getRol().name(),
                    usuario.getActivo()
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new UnauthorizedException("Credenciales inválidas");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        // En un sistema JWT stateless, el logout se maneja en el cliente
        // eliminando el token del almacenamiento local
        return ResponseEntity.ok("Logout exitoso");
    }
}
