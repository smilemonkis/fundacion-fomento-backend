package com.fundacionfomentodb.controller;

import com.fundacionfomentodb.dto.ProyectoResponse;
import com.fundacionfomentodb.entity.Proyecto;
import com.fundacionfomentodb.exception.ResourceNotFoundException;
import com.fundacionfomentodb.repository.ProyectoRepository;
import com.fundacionfomentodb.service.ImagenService;
import com.fundacionfomentodb.service.ProyectoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ImagenController {

    private final ImagenService imagenService;
    private final ProyectoRepository proyectoRepository;
    private final ProyectoService proyectoService;

    // POST /api/v1/proyectos/{id}/imagen
    @PostMapping("/proyectos/{id}/imagen")
    public ResponseEntity<ProyectoResponse> subirImagenProyecto(
            @PathVariable Integer id,
            @RequestParam("file") MultipartFile file) {

        Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado"));

        String url = imagenService.subirImagen(file, "proyectos");
        proyecto.setImagenUrl(url);
        proyectoRepository.save(proyecto);

        return ResponseEntity.ok(proyectoService.obtenerProyectoPorId(id));
    }

    // POST /api/v1/convocatorias/{id}/imagen
    @PostMapping("/convocatorias/{id}/imagen")
    public ResponseEntity<?> subirImagenConvocatoria(
            @PathVariable Integer id,
            @RequestParam("file") MultipartFile file) {

        // Por ahora retorna la URL — cuando tengas imagenUrl en Convocatoria lo conectamos
        String url = imagenService.subirImagen(file, "convocatorias");
        return ResponseEntity.ok(java.util.Map.of("imagenUrl", url));
    }
}