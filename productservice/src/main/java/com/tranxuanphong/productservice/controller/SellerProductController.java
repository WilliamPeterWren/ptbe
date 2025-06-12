package com.tranxuanphong.productservice.controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tranxuanphong.productservice.dto.request.ProductCreateRequest;
import com.tranxuanphong.productservice.dto.request.ProductUpdateRequest;
import com.tranxuanphong.productservice.dto.response.ApiResponse;
import com.tranxuanphong.productservice.dto.response.CartProductResponse;
import com.tranxuanphong.productservice.dto.response.FlashSaleProductResponse;
import com.tranxuanphong.productservice.dto.response.ProductResponse;
import com.tranxuanphong.productservice.service.ProductService;
import com.tranxuanphong.productservice.service.SellerProductService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/api/products/seller")
public class SellerProductController {
  SellerProductService sellerProductService;

  @PostMapping
  public ApiResponse<ProductResponse> create(@RequestBody ProductCreateRequest request) {
    return ApiResponse.<ProductResponse>builder()
      .result(sellerProductService.create(request))
      .build();
  }

  @PutMapping("/{id}")
  public ApiResponse<ProductResponse> updateById(@PathVariable String id, @RequestBody ProductUpdateRequest request) {
    return ApiResponse.<ProductResponse>builder()
    .result(sellerProductService.updateById(id, request))
    .build();
  }
  
  @DeleteMapping("/{id}")
  public ApiResponse<ProductResponse> delete(@PathVariable String id){
    return ApiResponse.<ProductResponse>builder()
    .result(sellerProductService.delete(id))
    .build();
  }
  
  @PostMapping("/set/product/images/id/{id}")
  public void setProductImageMetadata(@PathVariable String id, @RequestBody List<String> fileNames) {
    sellerProductService.saveProductImageMetadata(id, fileNames);
  }
}
