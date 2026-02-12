package com.fundacionfomentodb.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "donacion", indexes = {
        @Index(name = "idx_donacion_usuario", columnList = "usuario_id"),
        @Index(name = "idx_donacion_proyecto", columnList = "proyecto_id"),
        @Index(name = "idx_donacion_estado", columnList = "estado")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public
class Donacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal monto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DestinoEnum destino;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proyecto_id")
    private Proyecto proyecto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoEnum estado;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fecha;

    @PrePersist
    protected void onCreate() {
        this.fecha = LocalDateTime.now();
        if (this.estado == null) {
            this.estado = EstadoEnum.PENDIENTE;
        }
    }

    public enum DestinoEnum {
        LIBRE_INVERSION, PROYECTO_ACTIVO
    }

    public enum EstadoEnum {
        PENDIENTE, COMPLETADA, RECHAZADA, CANCELADA
    }
}
