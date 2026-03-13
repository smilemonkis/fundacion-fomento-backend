package com.fundacionfomentodb.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "noticia", indexes = {
        @Index(name = "idx_noticia_publicado", columnList = "publicado"),
        @Index(name = "idx_noticia_fecha",     columnList = "fechaPublicacion")
})
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Noticia {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 255)
    private String titulo;

    @Column(nullable = false, length = 500)
    private String resumen;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contenido;

    @Column(length = 500)
    private String imagenUrl;

    @Builder.Default
    @OneToMany(mappedBy = "noticia", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NoticiaImagen> galeria = new ArrayList<>();

    @Column(nullable = false, length = 150)
    private String autor;

    @Column(nullable = false)
    private Boolean publicado;

    @Column(nullable = false)
    private LocalDate fechaPublicacion;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.publicado == null) this.publicado = false;
    }

    @PreUpdate
    protected void onUpdate() { this.updatedAt = LocalDateTime.now(); }
}