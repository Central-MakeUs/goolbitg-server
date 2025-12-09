package com.goolbitg.api.v1.service;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.goolbitg.api.model.ImageUploadResponse;

/**
 * LocalImageService
 */
@Service
public class LocalImageService implements ImageService {

    @Value("${server.host}")
    private String hostname;

    @Value("${image.savedir}")
    private String imageSaveDir;

    private final String proto = "http";

    @Override
    public ImageUploadResponse uploadImage(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("Image file is empty");
        }

        try {
            // ensure directory exists
            Path saveDir = Paths.get(imageSaveDir);
            Files.createDirectories(saveDir);

            // original filename & extension
            String originalFilename = image.getOriginalFilename();
            String extension = "";

            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
            }

            // generate unique filename
            String savedFilename = UUID.randomUUID() + extension;
            Path targetPath = saveDir.resolve(savedFilename);

            // save file
            image.transferTo(targetPath.toFile());

            var response = new ImageUploadResponse();
            response.setUrl(URI.create(String.format("%s://%s/images/%s", proto, hostname, savedFilename)));
            return response;

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }

}
