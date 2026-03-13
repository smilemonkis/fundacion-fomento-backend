package com.fundacionfomentodb.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "parchate_imagen")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ParchateImagen {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parchate_id", nullable = false)
    private Parchate parchate;

    @Column(nullable = false, length = 500)
    private String url;

    @Column(length = 255)
    private String descripcion;

    @Column(nullable = false)
    @Builder.Default
    private Integer orden = 0;
}