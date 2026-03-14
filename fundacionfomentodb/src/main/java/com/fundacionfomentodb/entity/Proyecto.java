package com.fundacionfomentodb.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "proyecto", indexes = {
        @Index(name = "idx_proyecto_activo", columnList = "activo"),
        @Index(name = "idx_proyecto_estado", columnList = "estado")
})
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Proyecto {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 100)
    private String codigo;

    @Column(nullable = false, length = 255)
    private String nombre;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    // Fechas opcionales
    @Column
    private LocalDate fechaInicio;

    @Column
    private LocalDate fechaFin;

    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;

    // Estado manual controlado por el admin
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private EstadoEnum estado = EstadoEnum.ABIERTO;

    @Column(length = 500)
    private String imagenUrl;

    @Column
    private Integer beneficiarios;

    @Column(length = 50)
    private String presupuesto;

    @Column(columnDefinition = "INT DEFAULT 0")
    @Builder.Default
    private Integer progreso = 0;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public enum EstadoEnum { PROXIMAMENTE, ABIERTO, CERRADO }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.progreso == null) this.progreso = 0;
        if (this.activo   == null) this.activo   = true;
        if (this.estado   == null) this.estado   = EstadoEnum.ABIERTO;
    }

    @PreUpdate
    protected void onUpdate() { this.updatedAt = LocalDateTime.now(); }
}