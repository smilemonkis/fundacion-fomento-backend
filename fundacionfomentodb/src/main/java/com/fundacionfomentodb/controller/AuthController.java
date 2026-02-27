package com.fundacionfomentodb.controller;

import com.fundacionfomentodb.repository.AliadoJuridicoRepository;
import com.fundacionfomentodb.repository.AliadoNaturalRepository;
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
@CrossOrigin(origins = "http://localhost:8081")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;
    private final AliadoJuridicoRepository aliadoJuridicoRepository; // Añadir esta
    private final AliadoNaturalRepository aliadoNaturalRepository;

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

            // --- LÓGICA PARA CAPTURAR DATOS ESPECÍFICOS DEL ALIADO ---
            // 1. Inicializamos variables con los datos base del usuario
            String nombreFinal = usuario.getNombre();
            String razonSocial = null;
            String nit = null;

            // 2. Si el rol es Jurídico (Empresa), buscamos NIT y Razón Social en su tabla correspondiente
            if ("ALIADO_JUR".equals(usuario.getRol().name())) {
                // Buscamos al aliado jurídico por el ID del usuario
                var aj = aliadoJuridicoRepository.findByUsuarioId(usuario.getId()).orElse(null);
                if (aj != null) {
                    razonSocial = aj.getRazonSocial();
                    nit = aj.getNit();
                }
            }
            // 3. Si el rol es Natural (Persona), intentamos obtener el nombre desde su tabla específica
            else if ("ALIADO_NAT".equals(usuario.getRol().name())) {
                // Buscamos al aliado natural por el ID del usuario
                var an = aliadoNaturalRepository.findByUsuarioId(usuario.getId()).orElse(null);
                if (an != null) {
                    nombreFinal = an.getNombre();
                }
            }

            // 4. Construimos la respuesta final que viajará al Frontend (React)
            LoginResponse response = new LoginResponse(
                    token,
                    usuario.getId(),
                    nombreFinal,           // Nombre que se mostrará en el Dashboard
                    usuario.getApellido(),  // Apellido base de la tabla Usuario
                    razonSocial,            // Razón social (será null si es persona)
                    nit,                    // NIT (será null si es empresa)
                    usuario.getEmail(),
                    usuario.getRol().name(),
                    usuario.getActivo()
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
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
