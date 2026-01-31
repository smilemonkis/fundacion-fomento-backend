package com.fundacionfomentodb.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "inscripcion", indexes = {
        @Index(name = "idx_inscripcion_usuario", columnList = "usuario_id"),
        @Index(name = "idx_inscripcion_conv", columnList = "convocatoria_id"),
        @Index(name = "idx_inscripcion_estado", columnList = "estado"),
        @Index(name = "uk_inscripcion_usuario_convocatoria", columnList = "usuario_id,convocatoria_id", unique = true)
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "convocatoria_id", nullable = false)
    private Convocatoria convocatoria;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoEnum estado;

    @Column(length = 500)
    private String observaciones;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.estado == null) {
            this.estado = EstadoEnum.PENDIENTE;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public enum EstadoEnum {
        PENDIENTE, ACEPTADO, RECHAZADO
    }
}
