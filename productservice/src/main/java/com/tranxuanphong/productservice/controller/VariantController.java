package com.tranxuanphong.productservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tranxuanphong.productservice.dto.request.CreateVariantRequest;
import com.tranxuanphong.productservice.dto.request.UpdateVariantRequest;
import com.tranxuanphong.productservice.dto.response.ApiResponse;
import com.tranxuanphong.productservice.dto.response.VariantResponse;
import com.tranxuanphong.productservice.service.VariantService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/api/variants")
public class VariantController {
  VariantService variantService;


  @PostMapping
  public ApiResponse<VariantResponse> create(@RequestBody CreateVariantRequest request) {
    return ApiResponse.<VariantResponse>builder()
      .result(variantService.create(request))
      .build();
  }

  @GetMapping("/get-all")
  public ApiResponse<List<VariantResponse>> getAll() {
    return ApiResponse.<List<VariantResponse>>builder()
    .result(variantService.getAll())
    .build();
  }

  @GetMapping("/by-product/{productId}")
  public ApiResponse<List<VariantResponse>> getByProduct(@PathVariable String productId){
    return ApiResponse.<List<VariantResponse>>builder()
    .result(variantService.getByProductId(productId))
    .build();
  }

  @GetMapping("/{id}")
  public ApiResponse<VariantResponse> getOne(@PathVariable String id) {
    return ApiResponse.<VariantResponse>builder()
    .result(variantService.getOne(id))
    .build();
  }

  @PutMapping("/{id}")
  public ApiResponse<VariantResponse> upadte(@PathVariable String id, @RequestBody UpdateVariantRequest request) {
    return ApiResponse.<VariantResponse>builder()
    .result(variantService.update(id, request))
    .build();
  }
  
  @DeleteMapping("/{id}")
  public ApiResponse<String> delete(@PathVariable String id){
    variantService.delete(id);

    return ApiResponse.<String>builder()
    .result("Success delete variant")
    .build();
  }
}
