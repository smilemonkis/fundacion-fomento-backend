package com.fundacionfomentodb.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "aliado_destacado")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AliadoDestacado {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 150)
    private String nombre;

    // URL del logo subido a Cloudinary
    @Column(name = "logo_url", nullable = false, length = 500)
    private String logoUrl;

    // URL del sitio web — opcional
    @Column(name = "sitio_web", length = 500)
    private String sitioWeb;

    // Orden en el carrusel
    @Column(nullable = false)
    @Builder.Default
    private Integer orden = 0;

    // Visible en el home
    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}