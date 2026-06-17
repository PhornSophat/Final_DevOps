package com.example.demo.service;

import com.example.demo.config.StorageProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

@Service
public class PhotoStorageService {
    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024;
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of("image/jpeg", "image/png");

    private final Path photoDirectory;

    public PhotoStorageService(StorageProperties storageProperties) {
        this.photoDirectory = Paths.get(storageProperties.getPhotoDir()).toAbsolutePath().normalize();
    }

    public StoredPhoto store(MultipartFile file) {
        validate(file);
        try {
            Files.createDirectories(photoDirectory);
            String extension = extensionFor(file.getContentType());
            String fileName = UUID.randomUUID() + extension;
            Path target = photoDirectory.resolve(fileName).normalize();
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, target, StandardCopyOption.REPLACE_EXISTING);
            }
            return new StoredPhoto(fileName, file.getContentType());
        } catch (IOException ex) {
            throw new IllegalStateException("Could not store photo", ex);
        }
    }

    public Path load(String fileName) {
        if (!StringUtils.hasText(fileName)) {
            throw new IllegalArgumentException("Photo file name is required");
        }
        Path file = photoDirectory.resolve(fileName).normalize();
        if (!file.startsWith(photoDirectory)) {
            throw new IllegalArgumentException("Invalid photo file path");
        }
        return file;
    }

    private void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Photo is required");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("Photo must be 2MB or smaller");
        }
        if (!ALLOWED_CONTENT_TYPES.contains(file.getContentType())) {
            throw new IllegalArgumentException("Only JPEG and PNG photos are allowed");
        }
    }

    private String extensionFor(String contentType) {
        return "image/png".equals(contentType) ? ".png" : ".jpg";
    }

    public record StoredPhoto(String fileName, String contentType) {
    }
}
