package com.goolbitg.api.v1.service;

import org.springframework.web.multipart.MultipartFile;

import com.goolbitg.api.model.ImageUploadResponse;

/**
 * ImageService
 */
public interface ImageService {

    ImageUploadResponse uploadImage(MultipartFile image);
}
