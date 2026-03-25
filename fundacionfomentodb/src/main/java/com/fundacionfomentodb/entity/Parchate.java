package com.fundacionfomentodb.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "parchate", indexes = {
        @Index(name = "idx_parchate_tipo",      columnList = "tipo"),
        @Index(name = "idx_parchate_ubicacion", columnList = "ubicacion"),
        @Index(name = "idx_parchate_activo",    columnList = "activo")
})
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Parchate {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 255)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column(length = 500)
    private String imagenUrl;

    @Builder.Default
    @OneToMany(mappedBy = "parchate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParchateImagen> galeria = new ArrayList<>();

    @Column(nullable = false, length = 100)
    private String tipo;

    @Column(nullable = false, length = 150)
    private String ubicacion;

    @Column(length = 255)
    private String direccion;

    @Column(length = 500)
    private String urlMapa;

    // Enlace externo opcional (registro, más info, etc.)
    @Column(length = 500)
    private String enlace;

    @Column
    private LocalDateTime fechaEvento;

    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.activo == null) this.activo = true;
        normalizarUbicacion();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        normalizarUbicacion();
    }

    private void normalizarUbicacion() {
        if (this.ubicacion != null && !this.ubicacion.isBlank()) {
            this.ubicacion = this.ubicacion.substring(0, 1).toUpperCase()
                    + this.ubicacion.substring(1).toLowerCase();
        }
    }
}