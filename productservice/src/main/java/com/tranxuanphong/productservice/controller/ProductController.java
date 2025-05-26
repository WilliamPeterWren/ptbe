package com.tranxuanphong.productservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tranxuanphong.productservice.dto.request.ProductCreateRequest;
import com.tranxuanphong.productservice.dto.request.ProductUpdateRequest;
import com.tranxuanphong.productservice.dto.response.ApiResponse;
import com.tranxuanphong.productservice.dto.response.ProductResponse;
import com.tranxuanphong.productservice.entity.Product;
import com.tranxuanphong.productservice.service.ProductService;

import jakarta.ws.rs.Path;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
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
  public ApiResponse<ProductResponse> create(@RequestBody ProductCreateRequest request) {
    return ApiResponse.<ProductResponse>builder()
      .result(productService.create(request))
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

  @PutMapping("/{id}")
  public ApiResponse<ProductResponse> updateById(@PathVariable String id, @RequestBody ProductUpdateRequest request) {
    return ApiResponse.<ProductResponse>builder()
    .result(productService.updateById(id, request))
    .build();
  }
  
  @DeleteMapping("/{id}")
  public ApiResponse<ProductResponse> delete(@PathVariable String id){

    return ApiResponse.<ProductResponse>builder()
    .result(productService.delete(id))
    .build();
  }

  // @GetMapping("/search")
  // public ResponseEntity<Iterable<Product>> searchProducts(
  //       @RequestParam(required = false) String name,
  //       @RequestParam(required = false) String query,
  //       @RequestParam(required = false) String sellerId,
  //       @RequestParam(required = false) Double minPrice,
  //       @RequestParam(required = false) Double maxPrice) {
  //   return ResponseEntity.ok(productService.searchProductsByCriteria(name, query, sellerId, minPrice, maxPrice));
  // }

  @GetMapping("/check/product/id/{id}")
  public boolean checkProductId(@PathVariable String id) {
      return productService.checkProductId(id);
  }

  @GetMapping("/check/product/{productId}/seller/{sellerId}")
  public boolean checkProductBySellerId(@PathVariable String productId, @PathVariable String sellerId) {
    return productService.checkProductBySellerId(productId, sellerId);
  }

  @GetMapping("/get-products/seller/{sellerId}")
  public ApiResponse<Page<ProductResponse>> getProductsBySeller(@PathVariable String sellerId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
    return ApiResponse.<Page<ProductResponse>>builder()
    .result(productService.getPaginatedProductsBySellerId(sellerId, page, size))
    .build();
  }
  
  @GetMapping("/check/variant/id/{id}")
  public boolean doesVariantExist(@PathVariable String id) {
    return productService.doesVariantExist(id);
  }

  @GetMapping("/check/variant/id/{variantId}/seller/id/{sellerId}")
  public boolean doesVariantExistBySellerId(@PathVariable String variantId, @PathVariable String sellerId) {
    return productService.doesVariantExistBySellerId(variantId, sellerId);
  }

  

}
