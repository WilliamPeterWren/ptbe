package com.tranxuanphong.fileservice.controller;

import com.tranxuanphong.fileservice.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileController {
    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<List<String>> uploadFiles(@RequestParam("files") List<MultipartFile> files) {
        try {
            List<String> fileNames = fileService.saveFiles(files);
            return ResponseEntity.ok(fileNames);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(List.of(e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(List.of("Failed to upload files: " + e.getMessage()));
        }
    }

    @PostMapping("/update/{productId}")
    public ResponseEntity<List<String>> updateProductImages(
            @PathVariable String productId,
            @RequestParam("files") List<MultipartFile> files) {
        try {
            List<String> fileNames = fileService.updateProductImages(productId, files);
            return ResponseEntity.ok(fileNames);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(List.of(e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(List.of("Failed to update files: " + e.getMessage()));
        }
    }

    @PostMapping("/upload/review")
    public ResponseEntity<List<String>> uploadFilesReview(@RequestParam("files") List<MultipartFile> files) {
        try {
            List<String> fileNames = fileService.saveFilesReview(files);
            return ResponseEntity.ok(fileNames);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(List.of(e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(List.of("Failed to upload files: " + e.getMessage()));
        }
    }

    @PostMapping("/upload/avatar")
    public ResponseEntity<List<String>> uploadFilesAvatar(@RequestParam("files") List<MultipartFile> files) {
        try {
            List<String> fileNames = fileService.saveFilesAvatar(files);
            return ResponseEntity.ok(fileNames);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(List.of(e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(List.of("Failed to upload files: " + e.getMessage()));
        }
    }

    @PostMapping("/upload/order")
    public ResponseEntity<List<String>> uploadFilesOrder(@RequestParam("files") List<MultipartFile> files) {
        try {
            List<String> fileNames = fileService.saveFilesOrder(files);
            return ResponseEntity.ok(fileNames);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(List.of(e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(List.of("Failed to upload files: " + e.getMessage()));
        }
    }

    @DeleteMapping("/product")
    public void deleteFilesProduct(@RequestBody List<String> files) throws IOException{
        fileService.deleteFilesProduct(files);
    }

}