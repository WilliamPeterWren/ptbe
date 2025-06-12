package com.tranxuanphong.productservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tranxuanphong.productservice.dto.response.ApiResponse;
import com.tranxuanphong.productservice.dto.response.ProductResponse;
import com.tranxuanphong.productservice.service.AdminProductService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/api/products/admin")
public class AdminProductController {
  AdminProductService adminProductService;

  @GetMapping("/get-products")
  public ApiResponse<Page<ProductResponse>> getProducts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
    return ApiResponse.<Page<ProductResponse>>builder()
    .result(adminProductService.getPaginatedProducts(page, size))
    .build();
  }

  @DeleteMapping("/delete/admin/product")
  public void deleteByAdmin(){
    adminProductService.deleteByAdmin();
  }

  // @PostMapping("/update/all/shipping")
  // public void postMethodName(@RequestBody String entity) {
  //   productService.updateAllProductsWithDefaultAvailable();
  // }

  @PostMapping("/update/all/rating")
  public void postMethodName() {
    adminProductService.updateAllProductsWithDefaultAvailable();
  }
}
