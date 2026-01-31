package com.fundacionfomentodb.service;

import com.fundacionfomentodb.dto.*;
import com.fundacionfomentodb.entity.AliadoJuridico;
import com.fundacionfomentodb.entity.Usuario;
import com.fundacionfomentodb.mapper.AliadoJuridicoMapper;
import com.fundacionfomentodb.repository.AliadoJuridicoRepository;
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
public class AliadoJuridicoService {
    
    private final AliadoJuridicoRepository aliadoJuridicoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AliadoJuridicoMapper aliadoJuridicoMapper;
    private final PasswordEncoder passwordEncoder;
    
    public AliadoJuridicoResponse crearAliadoJuridico(CreateAliadoJuridicoRequest request) {
        if (aliadoJuridicoRepository.findByNit(request.nit()).isPresent()) {
            throw new BadRequestException("El NIT " + request.nit() + " ya está registrado");
        }
        
        if (usuarioRepository.findByEmail(request.email()).isPresent()) {
            throw new BadRequestException("El email " + request.email() + " ya está registrado");
        }
        
        Usuario usuario = Usuario.builder()
            .email(request.email())
            .passwordHash(passwordEncoder.encode(request.password()))
            .rol(Usuario.RolEnum.ALIADO_JUR)
            .activo(true)
            .build();
        
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        
        AliadoJuridico aliado = aliadoJuridicoMapper.toEntity(request);
        aliado.setUsuario(usuarioGuardado);
        
        AliadoJuridico aliadoGuardado = aliadoJuridicoRepository.save(aliado);
        return aliadoJuridicoMapper.toResponseDto(aliadoGuardado);
    }
    
    @Transactional(readOnly = true)
    public AliadoJuridicoResponse obtenerAliadoPorId(Integer id) {
        return aliadoJuridicoRepository.findById(id)
            .map(aliadoJuridicoMapper::toResponseDto)
            .orElseThrow(() -> new ResourceNotFoundException("Aliado jurídico no encontrado"));
    }
    
    @Transactional(readOnly = true)
    public AliadoJuridicoResponse obtenerAliadoPorNit(String nit) {
        return aliadoJuridicoRepository.findByNit(nit)
            .map(aliadoJuridicoMapper::toResponseDto)
            .orElseThrow(() -> new ResourceNotFoundException("Aliado con NIT " + nit + " no encontrado"));
    }
    
    @Transactional(readOnly = true)
    public Page<AliadoJuridicoResponse> listarAliados(SearchAliadoJuridicoRequest request) {
        Pageable pageable = PageRequest.of(
            request.pageNumber() != null ? request.pageNumber() : 0,
            request.pageSize() != null ? request.pageSize() : 10
        );
        
        Page<AliadoJuridico> resultado;
        
        if (request.nit() != null && !request.nit().isBlank()) {
            resultado = aliadoJuridicoRepository.findByNit(request.nit(), pageable);
        } else if (request.razonSocial() != null && !request.razonSocial().isBlank()) {
            resultado = aliadoJuridicoRepository.findByRazonSocialContainingIgnoreCase(request.razonSocial(), pageable);
        } else {
            resultado = aliadoJuridicoRepository.findAll(pageable);
        }
        
        return resultado.map(aliadoJuridicoMapper::toResponseDto);
    }
    
    public AliadoJuridicoResponse actualizarAliadoJuridico(Integer id, UpdateAliadoJuridicoRequest request) {
        AliadoJuridico aliado = aliadoJuridicoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Aliado jurídico no encontrado"));
        
        if (request.email() != null && !request.email().isBlank() 
            && !aliado.getEmail().equals(request.email())) {
            
            if (usuarioRepository.findByEmail(request.email()).isPresent()) {
                throw new BadRequestException("El email ya está en uso");
            }
        }
        
        aliadoJuridicoMapper.updateEntity(request, aliado);
        AliadoJuridico aliadoActualizado = aliadoJuridicoRepository.save(aliado);
        
        return aliadoJuridicoMapper.toResponseDto(aliadoActualizado);
    }
    
    public void eliminarAliadoJuridico(Integer id) {
        AliadoJuridico aliado = aliadoJuridicoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Aliado jurídico no encontrado"));
        
        Usuario usuario = aliado.getUsuario();
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
        
        aliadoJuridicoRepository.delete(aliado);
    }
}
