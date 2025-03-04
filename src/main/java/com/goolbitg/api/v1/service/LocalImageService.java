package com.goolbitg.api.v1.service;

import org.springframework.web.multipart.MultipartFile;

import com.goolbitg.api.model.ImageUploadResponse;

/**
 * LocalImageService
 */
public class LocalImageService implements ImageService {

    @Override
    public ImageUploadResponse uploadImage(MultipartFile image) {
        throw new UnsupportedOperationException("Unimplemented method 'uploadImage'");
    }


}
