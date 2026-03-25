package com.fundacionfomentodb.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "metrica")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Metrica {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String label;

    @Column(nullable = false, length = 50)
    private String valor;

    // Nombre del icono: Users, FolderOpen, MapPin, Handshake
    @Column(nullable = false, length = 50)
    @Builder.Default
    private String icono = "Users";

    @Column(nullable = false)
    @Builder.Default
    private Integer orden = 0;

    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}