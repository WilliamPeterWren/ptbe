package com.tranxuanphong.peterservice.controller;


import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tranxuanphong.peterservice.dto.request.PeterCategoryCreateRequest;
import com.tranxuanphong.peterservice.dto.request.PeterCategoryUpdateRequest;
import com.tranxuanphong.peterservice.dto.response.ApiResponse;
import com.tranxuanphong.peterservice.dto.response.PeterCategoryResponse;
import com.tranxuanphong.peterservice.service.PeterCategoryService;

// import jakarta.validation.Valid;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/api/petercategories")
public class PeterCategoryController{
  PeterCategoryService peterCategoryService;

  @PostMapping
  public ApiResponse<PeterCategoryResponse> create(@RequestBody PeterCategoryCreateRequest request) {
    return ApiResponse.<PeterCategoryResponse>builder()
    .result(peterCategoryService.create(request))
    .build();
  }

  @GetMapping
  public ApiResponse<List<PeterCategoryResponse>> getAll() {
    return ApiResponse.<List<PeterCategoryResponse>>builder()
    .result(peterCategoryService.getAll())
    .build();
  } 

  @GetMapping("/{id}")
  public ApiResponse<PeterCategoryResponse> getOne(@PathVariable String id) {
    return ApiResponse.<PeterCategoryResponse>builder()
    .result(peterCategoryService.getOne(id))
    .build();
  } 

  @PutMapping("/{id}")
  public ApiResponse<PeterCategoryResponse> update(@PathVariable String id, @RequestBody PeterCategoryUpdateRequest request) {
    return ApiResponse.<PeterCategoryResponse>builder()
    .result(peterCategoryService.update(id, request))
    .build();
  } 
  
  @GetMapping("/exists-by-id/{id}")
  public boolean existsById(@PathVariable String id) {
    return peterCategoryService.existsById(id);
  }


}
