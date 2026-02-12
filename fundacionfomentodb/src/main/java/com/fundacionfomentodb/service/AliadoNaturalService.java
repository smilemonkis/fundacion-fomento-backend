package com.fundacionfomentodb.service;

import com.fundacionfomentodb.dto.*;
import com.fundacionfomentodb.entity.AliadoNatural;
import com.fundacionfomentodb.entity.Usuario;
import com.fundacionfomentodb.mapper.AliadoNaturalMapper;
import com.fundacionfomentodb.repository.AliadoNaturalRepository;
import com.fundacionfomentodb.repository.UsuarioRepository;
import com.fundacionfomentodb.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AliadoNaturalService {
    
    private final AliadoNaturalRepository aliadoNaturalRepository;
    private final UsuarioRepository usuarioRepository;
    private final AliadoNaturalMapper aliadoNaturalMapper;
    private final PasswordEncoder passwordEncoder;
    
    public AliadoNaturalResponse crearAliadoNatural(CreateAliadoNaturalRequest request) {
        if (aliadoNaturalRepository.findByDocumento(request.documento()).isPresent()) {
            throw new BadRequestException("El documento " + request.documento() + " ya está registrado");
        }
        
        if (usuarioRepository.findByEmail(request.email()).isPresent()) {
            throw new BadRequestException("El email " + request.email() + " ya está registrado");
        }
        
        Usuario usuario = Usuario.builder()
            .email(request.email())
            .passwordHash(passwordEncoder.encode(request.password()))
            .rol(Usuario.RolEnum.ALIADO_NAT)
            .activo(true)
            .build();
        
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        
        AliadoNatural aliado = aliadoNaturalMapper.toEntity(request);
        aliado.setUsuario(usuarioGuardado);
        
        AliadoNatural aliadoGuardado = aliadoNaturalRepository.save(aliado);
        return aliadoNaturalMapper.toResponseDto(aliadoGuardado);
    }
    
    @Transactional(readOnly = true)
    public AliadoNaturalResponse obtenerAliadoPorId(Integer id) {
        return aliadoNaturalRepository.findById(id)
            .map(aliadoNaturalMapper::toResponseDto)
            .orElseThrow(() -> new ResourceNotFoundException("Aliado natural no encontrado"));
    }
    
    @Transactional(readOnly = true)
    public AliadoNaturalResponse obtenerAliadoPorDocumento(String documento) {
        return aliadoNaturalRepository.findByDocumento(documento)
            .map(aliadoNaturalMapper::toResponseDto)
            .orElseThrow(() -> new ResourceNotFoundException("Aliado con documento " + documento + " no encontrado"));
    }
    
    @Transactional(readOnly = true)
    public Page<AliadoNaturalResponse> listarAliados(SearchAliadoNaturalRequest request) {
        Pageable pageable = PageRequest.of(
            request.pageNumber() != null ? request.pageNumber() : 0,
            request.pageSize() != null ? request.pageSize() : 10
        );
        
        Page<AliadoNatural> resultado;
        
        if (request.documento() != null && !request.documento().isBlank()) {
            resultado = aliadoNaturalRepository.findByDocumento(request.documento(), pageable);
        } else if (request.nombre() != null && !request.nombre().isBlank()) {
            resultado = aliadoNaturalRepository.findByNombreContainingIgnoreCase(request.nombre(), pageable);
        } else {
            resultado = aliadoNaturalRepository.findAll(pageable);
        }
        
        return resultado.map(aliadoNaturalMapper::toResponseDto);
    }
    
    public AliadoNaturalResponse actualizarAliadoNatural(Integer id, UpdateAliadoNaturalRequest request) {
        AliadoNatural aliado = aliadoNaturalRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Aliado natural no encontrado"));
        
        if (request.email() != null && !request.email().isBlank() 
            && !aliado.getUsuario().getEmail().equals(request.email())) {
            
            if (usuarioRepository.findByEmail(request.email()).isPresent()) {
                throw new BadRequestException("El email ya está en uso");
            }
        }
        
        aliadoNaturalMapper.updateEntity(request, aliado);
        
        AliadoNatural aliadoActualizado = aliadoNaturalRepository.save(aliado);
        return aliadoNaturalMapper.toResponseDto(aliadoActualizado);
    }
    
    public void eliminarAliadoNatural(Integer id) {
        AliadoNatural aliado = aliadoNaturalRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Aliado natural no encontrado"));
        
        Usuario usuario = aliado.getUsuario();
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
        
        aliadoNaturalRepository.delete(aliado);
    }
}
