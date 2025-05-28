package com.tranxuanphong.productservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tranxuanphong.productservice.dto.request.CategoryCreateRequest;
import com.tranxuanphong.productservice.dto.request.CategoryUpdateRequest;
import com.tranxuanphong.productservice.dto.response.ApiResponse;
import com.tranxuanphong.productservice.dto.response.CategoryResponse;
import com.tranxuanphong.productservice.service.CategoryService;

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
@RequestMapping("/api/categories")
public class CategoryController {
  CategoryService categoryService;

  @PostMapping
  public ApiResponse<CategoryResponse> create(@RequestBody CategoryCreateRequest request) {
    return ApiResponse.<CategoryResponse>builder()
      .result(categoryService.create(request))
      .build();
  }

  @GetMapping("/get-all")
  public ApiResponse<List<CategoryResponse>> getAll() {
    return ApiResponse.<List<CategoryResponse>>builder()
    .result(categoryService.getAll())
    .build();
  }

  @GetMapping("/get-by-sellerid/{sellerId}")
  public ApiResponse<List<CategoryResponse>> getBySellerID(@PathVariable String sellerId) {
    return ApiResponse.<List<CategoryResponse>>builder()
    .result(categoryService.getBySellerID(sellerId))
    .build();
  }

  @GetMapping("/{slug}")
  public ApiResponse<CategoryResponse> getOne(@PathVariable String slug) {
    return ApiResponse.<CategoryResponse>builder()
    .result(categoryService.getOne(slug))
    .build();
  }

  @PutMapping("/{slug}")
  public ApiResponse<CategoryResponse> upadte(@PathVariable String slug, @RequestBody CategoryUpdateRequest request) {
    return ApiResponse.<CategoryResponse>builder()
    .result(categoryService.update(slug, request))
    .build();
  }
  
  @DeleteMapping("/{id}")
  public ApiResponse<String> delete(@PathVariable String id){
    categoryService.delete(id);

    return ApiResponse.<String>builder()
    .result("Success delete category")
    .build();
  }

  //  @GetMapping("/search")
  //   public ApiResponse<List<Category>> searchCategories(
  //           @RequestParam(required = false) String name,
  //           @RequestParam(required = false) String sellerId) {
  //       return ApiResponse.<List<Category>>builder()
  //               .result(categoryService.searchCategoriesByCriteria(name, sellerId))
  //               .build();
  //   }
}
