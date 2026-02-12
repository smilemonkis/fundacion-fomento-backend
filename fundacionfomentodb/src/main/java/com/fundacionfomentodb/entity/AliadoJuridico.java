package com.fundacionfomentodb.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "aliado_juridico")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AliadoJuridico {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;
    
    @Column(nullable = false, unique = true, length = 100)
    private String nit;
    
    @Column(nullable = false, length = 255)
    private String razonSocial;
    
    @Column(nullable = false, length = 255)
    private String representante;
    
    @Column(nullable = false, length = 255)
    private String email;
    
    @Column(nullable = false, length = 100)
    private String telefono;
    
    @Column(nullable = false, length = 255)
    private String direccion;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
