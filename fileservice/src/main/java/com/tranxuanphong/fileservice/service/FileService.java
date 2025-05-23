package com.tranxuanphong.fileservice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService {
    private static final String UPLOAD_DIR = "/uploads/images/";

    public String saveFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }
        if (!file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("File must be an image");
        }

        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR, fileName);

        Files.createDirectories(filePath.getParent());

        Files.write(filePath, file.getBytes());

        return fileName;
    }
}