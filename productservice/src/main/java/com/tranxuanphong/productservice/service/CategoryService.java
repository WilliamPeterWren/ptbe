package com.tranxuanphong.productservice.service;


import org.springframework.stereotype.Service;

import com.tranxuanphong.productservice.dto.request.CreateCategoryRequest;
import com.tranxuanphong.productservice.dto.request.UpdateCategoryRequest;
import com.tranxuanphong.productservice.dto.response.CategoryResponse;
import com.tranxuanphong.productservice.entity.Category;
import com.tranxuanphong.productservice.exception.AppException;
import com.tranxuanphong.productservice.exception.ErrorCode;
import com.tranxuanphong.productservice.mapper.CategoryMapper;
import com.tranxuanphong.productservice.repository.CategoryRepository;
import com.tranxuanphong.productservice.repository.httpclient.UserClient;
import com.tranxuanphong.productservice.utils.GenerateSlug;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Slf4j
public class CategoryService {
  CategoryRepository categoryRepository;
  CategoryMapper categoryMapper;
  UserClient userClient;

  GenerateSlug generateSlug;

  public CategoryResponse create(CreateCategoryRequest request){

    if(categoryRepository.existsByCategoryName(request.getCategoryName())){
      throw new AppException(ErrorCode.CATEGORY_EXISTS);
    }

    boolean response = userClient.existsId(request.getSellerId());

    if(!response){
      throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    String slug = generateSlug.generateSlug(request.getCategoryName());

    request.setSlug(slug);

    Category category = categoryMapper.toCategory(request);

    return categoryMapper.toCategoryResponse(categoryRepository.save(category));
  }

  public List<CategoryResponse> getAll(){
    return categoryMapper.toListCategoryResponse(categoryRepository.findAll());
  }

  public List<CategoryResponse> getBySellerID(String sellerId){
    return categoryMapper.toListCategoryResponse(categoryRepository.findBySellerId(sellerId));
  }

  public CategoryResponse getOne(String slug){
    Category category = categoryRepository.findBySlug(slug).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTS));
    return categoryMapper.toCategoryResponse(category);
  }

  public CategoryResponse update(String slug, UpdateCategoryRequest request){
    Category category = categoryRepository.findBySlug(slug).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTS));
    
    if(categoryRepository.existsByCategoryName(request.getCategoryName())){
      throw new AppException(ErrorCode.CATEGORY_EXISTS);
    }

    categoryMapper.updateCategory(category, request);
    
    return categoryMapper.toCategoryResponse(category);
  }

  public void delete(String id){
    categoryRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTS));

    categoryRepository.deleteById(id);
  }

}
