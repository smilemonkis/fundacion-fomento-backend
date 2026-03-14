package com.fundacionfomentodb.service;

import com.fundacionfomentodb.dto.*;
import com.fundacionfomentodb.entity.Usuario;
import com.fundacionfomentodb.exception.BadRequestException;
import com.fundacionfomentodb.exception.ResourceNotFoundException;
import com.fundacionfomentodb.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder   passwordEncoder;

    public UsuarioResponse crearUsuario(CreateUsuarioRequest request) {
        if (usuarioRepository.findByEmail(request.email()).isPresent()) {
            throw new BadRequestException("El email " + request.email() + " ya está registrado");
        }
        Usuario usuario = Usuario.builder()
                .nombre(request.nombre())
                .apellido(request.apellido())
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .rol(Usuario.RolEnum.valueOf(request.rol()))
                .activo(true)
                .build();
        return toResponseDto(usuarioRepository.save(usuario));
    }

    @Transactional(readOnly = true)
    public UsuarioResponse obtenerUsuarioPorId(Integer id) {
        return usuarioRepository.findById(id)
                .map(this::toResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }

    @Transactional(readOnly = true)
    public Page<UsuarioResponse> listarUsuarios(String email, String rol, Pageable pageable) {
        Page<Usuario> resultado;
        if (email != null && !email.isBlank()) {
            resultado = usuarioRepository.findByEmailContainingIgnoreCase(email, pageable);
        } else if (rol != null && !rol.isBlank()) {
            resultado = usuarioRepository.findByRol(Usuario.RolEnum.valueOf(rol), pageable);
        } else {
            resultado = usuarioRepository.findAll(pageable);
        }
        return resultado.map(this::toResponseDto);
    }

    public UsuarioResponse actualizarUsuario(Integer id, UpdateUsuarioRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (request.email() != null && !request.email().isBlank()
                && !usuario.getEmail().equals(request.email())) {
            if (usuarioRepository.findByEmail(request.email()).isPresent()) {
                throw new BadRequestException("El email ya está en uso");
            }
            usuario.setEmail(request.email());
        }
        if (request.password() != null && !request.password().isBlank()) {
            usuario.setPasswordHash(passwordEncoder.encode(request.password()));
        }
        if (request.rol() != null && !request.rol().isBlank()) {
            usuario.setRol(Usuario.RolEnum.valueOf(request.rol()));
        }
        return toResponseDto(usuarioRepository.save(usuario));
    }

    // ── NUEVO: cambiar contraseña verificando la actual ───────
    public void cambiarPassword(Integer id, CambiarPasswordRequest req) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (!passwordEncoder.matches(req.passwordActual(), usuario.getPasswordHash())) {
            throw new BadRequestException("La contraseña actual es incorrecta");
        }
        if (req.passwordNueva().length() < 8) {
            throw new BadRequestException("La nueva contraseña debe tener al menos 8 caracteres");
        }
        usuario.setPasswordHash(passwordEncoder.encode(req.passwordNueva()));
        usuarioRepository.save(usuario);
    }

    public UsuarioResponse activarUsuario(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        usuario.setActivo(true);
        return toResponseDto(usuarioRepository.save(usuario));
    }

    public UsuarioResponse desactivarUsuario(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        usuario.setActivo(false);
        return toResponseDto(usuarioRepository.save(usuario));
    }

    private UsuarioResponse toResponseDto(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getRol().name(),
                usuario.getActivo(),
                usuario.getCreatedAt(),
                usuario.getUpdatedAt()
        );
    }
}