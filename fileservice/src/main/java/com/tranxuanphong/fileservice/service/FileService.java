package com.tranxuanphong.fileservice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tranxuanphong.fileservice.httpclient.ProductClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class FileService {
    String UPLOAD_DIR = System.getProperty("user.dir") + "/fileservice/src/main/resources/static/images/product";

    ProductClient productClient;

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

    public List<String> updateProductImages(String productId, List<MultipartFile> files) throws IOException {
        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException("File list cannot be empty");
        }

        // Step 1: Delete old images for the product
        deleteOldImages(productId);

        // Step 2: Save new images
        List<String> fileNames = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                throw new IllegalArgumentException("File cannot be empty: " + file.getOriginalFilename());
            }
            if (!file.getContentType().startsWith("image/")) {
                throw new IllegalArgumentException("File must be an image: " + file.getOriginalFilename());
            }

            // Optionally prepend productId to ensure unique filenames
            String fileName = file.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR, fileName);

            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());

            fileNames.add(fileName);
        }

        // Step 3: Save the new list of filenames (e.g., to a database or file)
        saveProductImageMetadata(productId, fileNames);

        return fileNames;
    }

    // Delete old images for a product
    private void deleteOldImages(String productId) throws IOException {
        // Retrieve the list of old image filenames for the product
        List<String> oldImageNames = getProductImageMetadata(productId);

        if (oldImageNames != null) {
            for (String imageName : oldImageNames) {
                Path imagePath = Paths.get(UPLOAD_DIR, imageName);
                if (Files.exists(imagePath)) {
                    Files.delete(imagePath);
                }
            }
        }
    }

    private List<String> getProductImageMetadata(String productId) {
        List<String> productImages = productClient.getProductImageMetadata(productId);
        return productImages; 
    }

    // Mock method to save image metadata
    private void saveProductImageMetadata(String productId, List<String> fileNames) {
        
        productClient.setProductImageMetadata(productId, fileNames);
        System.out.println("Saving metadata for product " + productId + ": " + fileNames);

    }

}