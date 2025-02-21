package com.goolbitg.api.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.goolbitg.api.model.ImageUploadResponse;

/**
 * LocalImageService
 */
@Service
@Profile("!dev")
public class LocalImageService implements ImageService {

    @Override
    public ImageUploadResponse uploadImage(MultipartFile image) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'uploadImage'");
    }


}
