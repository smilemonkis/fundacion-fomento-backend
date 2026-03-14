package com.fundacionfomentodb.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "convocatoria", indexes = {
        @Index(name = "idx_conv_activa",  columnList = "activa"),
        @Index(name = "idx_conv_fechas",  columnList = "fecha_inicio,fecha_fin"),
        @Index(name = "idx_conv_estado",  columnList = "estado")
})
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Convocatoria {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 255)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    // Fechas opcionales
    @Column
    private LocalDate fechaInicio;

    @Column
    private LocalDate fechaFin;

    @Column(nullable = false)
    @Builder.Default
    private Boolean activa = true;

    // Estado manual controlado por el admin
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private EstadoEnum estado = EstadoEnum.ABIERTO;

    @Column(length = 500)
    private String imagenUrl;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public enum EstadoEnum {
        PROXIMAMENTE, ABIERTO, CERRADO
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.estado == null) this.estado = EstadoEnum.ABIERTO;
        if (this.activa == null) this.activa = true;
    }

    @PreUpdate
    protected void onUpdate() { this.updatedAt = LocalDateTime.now(); }
}