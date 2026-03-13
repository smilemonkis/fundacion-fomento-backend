package com.fundacionfomentodb.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "oportunidad", indexes = {
        @Index(name = "idx_oportunidad_tipo",   columnList = "tipo"),
        @Index(name = "idx_oportunidad_activo", columnList = "activo")
})
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Oportunidad {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 255)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column(length = 500)
    private String imagenUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoEnum tipo;

    @Column
    private LocalDate fechaLimite;

    @Column(nullable = false)
    private Boolean activo;

    @Column(length = 500)
    private String enlace;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public enum TipoEnum {
        EMPLEO, FORMACION, VOLUNTARIADO
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.activo == null) this.activo = true;
    }

    @PreUpdate
    protected void onUpdate() { this.updatedAt = LocalDateTime.now(); }
}