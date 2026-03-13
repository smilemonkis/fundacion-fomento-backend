package com.fundacionfomentodb.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "suscripcion", indexes = {
        @Index(name = "idx_suscripcion_email",  columnList = "email", unique = true),
        @Index(name = "idx_suscripcion_activo", columnList = "activo")
})
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Suscripcion {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false)
    private Boolean activo;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaSuscripcion;

    @PrePersist
    protected void onCreate() {
        this.fechaSuscripcion = LocalDateTime.now();
        if (this.activo == null) this.activo = true;
    }
}