package com.fundacionfomentodb.repository;

import com.fundacionfomentodb.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByEmailAndActivoTrue(String email);
    Page<Usuario> findByRol(Usuario.RolEnum rol, Pageable pageable);
    Page<Usuario> findByEmailContainingIgnoreCase(String email, Pageable pageable);
}

