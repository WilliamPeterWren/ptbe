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
    String UPLOAD_DIR_REVIEW = System.getProperty("user.dir") + "/fileservice/src/main/resources/static/images/review";
    String UPLOAD_DIR_AVATAR = System.getProperty("user.dir") + "/fileservice/src/main/resources/static/images/avatar";
    String UPLOAD_DIR_ORDER = System.getProperty("user.dir") + "/fileservice/src/main/resources/static/images/order";

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

        deleteOldImages(productId);

        List<String> fileNames = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                throw new IllegalArgumentException("File cannot be empty: " + file.getOriginalFilename());
            }
            if (!file.getContentType().startsWith("image/")) {
                throw new IllegalArgumentException("File must be an image: " + file.getOriginalFilename());
            }

            String fileName = file.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR, fileName);

            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());

            fileNames.add(fileName);
        }

        saveProductImageMetadata(productId, fileNames);

        return fileNames;
    }

    private void deleteOldImages(String productId) throws IOException {
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

    private void saveProductImageMetadata(String productId, List<String> fileNames) {
        
        productClient.setProductImageMetadata(productId, fileNames);
        System.out.println("Saving metadata for product " + productId + ": " + fileNames);

    }

    public List<String> saveFilesReview(List<MultipartFile> files) throws IOException {
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
            Path filePath = Paths.get(UPLOAD_DIR_REVIEW, fileName);

            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());

            fileNames.add(fileName);
        }
        return fileNames;
    }

    public List<String> saveFilesAvatar(List<MultipartFile> files) throws IOException {
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
            Path filePath = Paths.get(UPLOAD_DIR_AVATAR, fileName);

            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());

            fileNames.add(fileName);
        }
        return fileNames;
    }
    
    public List<String> saveFilesOrder(List<MultipartFile> files) throws IOException {
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
            Path filePath = Paths.get(UPLOAD_DIR_ORDER, fileName);

            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());

            fileNames.add(fileName);
        }
        return fileNames;
    }

    public void deleteFilesAvatar(List<String> fileNames) throws IOException {
        if (fileNames == null || fileNames.isEmpty()) {
            throw new IllegalArgumentException("File name list cannot be empty");
        }
    
        for (String fileName : fileNames) {
            Path filePath = Paths.get(UPLOAD_DIR_AVATAR, fileName);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                System.out.println("Deleted file: " + filePath);
            } else {
                System.out.println("File not found, could not delete: " + filePath);
            }
        }
    }

    public void deleteFilesProduct(List<String> fileNames) throws IOException {
        if (fileNames == null || fileNames.isEmpty()) {
            throw new IllegalArgumentException("File name list cannot be empty");
        }
    
        for (String fileName : fileNames) {
            Path filePath = Paths.get(UPLOAD_DIR, fileName);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                System.out.println("Deleted file: " + filePath);
            } else {
                System.out.println("File not found, could not delete: " + filePath);
            }
        }
    }
    
}