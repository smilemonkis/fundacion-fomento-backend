package com.fundacionfomentodb.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fundacionfomentodb.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImagenService {

    private final Cloudinary cloudinary;

    public String subirImagen(MultipartFile file, String carpeta) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("El archivo no puede estar vacío");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new BadRequestException("Solo se permiten imágenes");
        }

        try {
            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder",          "fundacion/" + carpeta,
                            "resource_type",   "image",
                            "transformation",  ObjectUtils.asMap(
                                    "width",   800,
                                    "height",  500,
                                    "crop",    "fill",
                                    "quality", "auto"
                            )
                    )
            );
            return (String) uploadResult.get("secure_url");
        } catch (IOException e) {
            throw new BadRequestException("Error al subir la imagen: " + e.getMessage());
        }
    }
}