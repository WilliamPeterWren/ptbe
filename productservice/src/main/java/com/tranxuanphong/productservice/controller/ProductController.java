package com.tranxuanphong.productservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tranxuanphong.productservice.dto.request.CreateProductRequest;
import com.tranxuanphong.productservice.dto.request.UpdateProductRequest;
import com.tranxuanphong.productservice.dto.response.ApiResponse;
import com.tranxuanphong.productservice.dto.response.ProductResponse;
import com.tranxuanphong.productservice.service.ProductService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
  ProductService productService;


  @PostMapping
  public ApiResponse<ProductResponse> create(@RequestBody CreateProductRequest request) {
    return ApiResponse.<ProductResponse>builder()
      .result(productService.create(request))
      .build();
  }

  @GetMapping("/get-all")
  public ApiResponse<List<ProductResponse>> getAll() {
    return ApiResponse.<List<ProductResponse>>builder()
    .result(productService.getAll())
    .build();
  }

  @GetMapping("/get-products")
  public ApiResponse<Page<ProductResponse>> getProducts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
    return ApiResponse.<Page<ProductResponse>>builder()
    .result(productService.getPaginatedProducts(page, size))
    .build();
  }

  @GetMapping("/{slug}")
  public ApiResponse<ProductResponse> getOne(@PathVariable String slug) {
    return ApiResponse.<ProductResponse>builder()
    .result(productService.getOne(slug))
    .build();
  }

  @PutMapping("/{slug}")
  public ApiResponse<ProductResponse> upadte(@PathVariable String slug, @RequestBody UpdateProductRequest request) {
    return ApiResponse.<ProductResponse>builder()
    .result(productService.update(slug, request))
    .build();
  }
  
  @DeleteMapping("/{id}")
  public ApiResponse<String> delete(@PathVariable String id){
    productService.delete(id);

    return ApiResponse.<String>builder()
    .result("Success delete product")
    .build();
  }
}
