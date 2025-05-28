package com.tranxuanphong.fileservice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/fileservice/src/main/resources/static/images/product";

    public List<String> saveFiles(List<MultipartFile> files) throws IOException {
        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException("File list cannot be empty");
        }

        List<String> fileNames = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                throw new IllegalArgumentException("File cannot be empty: " + file.getOriginalFilename());
            }
            if (!file.getContentType().startsWith("image/")) {
                throw new IllegalArgumentException("File must be an image: " + file.getOriginalFilename());
            }

            // String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            String fileName = file.getOriginalFilename();
            System.out.println("original name: " + fileName);
            Path filePath = Paths.get(UPLOAD_DIR, fileName);

            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());

            fileNames.add(fileName);
        }
        return fileNames;
    }
}