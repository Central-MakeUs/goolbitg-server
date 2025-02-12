package com.goolbitg.api.service;

import java.io.InputStream;
import java.net.URI;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.goolbitg.api.exception.CommonException;
import com.goolbitg.api.exception.EtcException;
import com.goolbitg.api.model.ImageUploadResponse;

import lombok.RequiredArgsConstructor;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

/**
 * ImageServiceImpl
 */
@Service
@RequiredArgsConstructor
@Profile("dev")
public class S3ImageService implements ImageService {

    @Autowired
    private final S3Client s3Client;

    @Value("${aws.s3.host}")
    private String s3Host;

    private final String BUCKET = "goolbitg-bucket";
    private final String DIR_ROOT = "upload";

    @Override
    public ImageUploadResponse uploadImage(MultipartFile image) {
        String contentType = image.getContentType();
        if (!validateImageType(contentType)) {
            throw EtcException.imageTypeNotAllowed(contentType);
        }

        String key = createFilename(contentType);
        PutObjectRequest request = PutObjectRequest.builder()
            .bucket(BUCKET)
            .key(key)
            .contentType(image.getContentType())
            .build();

        try {
            InputStream inputStream = image.getInputStream();
            s3Client.putObject(request, RequestBody.fromInputStream(inputStream, inputStream.available()));
        } catch (Exception e) {
            throw new CommonException(1000, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String url = s3Host + "/" + key;

        ImageUploadResponse dto = new ImageUploadResponse();
        dto.setUrl(URI.create(url));
        return dto;
    }

    private String createFilename(String contentType) {
        String extension = contentType.split("\\/")[1];
        return DIR_ROOT + "/" + UUID.randomUUID() + "." + extension;
    }

    private boolean validateImageType(String contentType) {
        return contentType.equals("image/png") ||
                contentType.equals("image/jpeg") ||
                contentType.equals("image/jpg");
    }

}
