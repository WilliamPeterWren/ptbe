package com.tranxuanphong.productservice.mapper;


import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.tranxuanphong.productservice.dto.request.CreateCategoryRequest;
import com.tranxuanphong.productservice.dto.request.UpdateCategoryRequest;
import com.tranxuanphong.productservice.dto.response.CategoryResponse;
import com.tranxuanphong.productservice.entity.Category;


@Mapper(componentModel = "spring") 
public interface CategoryMapper {

  Category toCategory(CreateCategoryRequest request); 
  void updateCategory(@MappingTarget Category category, UpdateCategoryRequest request); 
  CategoryResponse toCategoryResponse(Category category); 
  List<CategoryResponse> toListCategoryResponse(List<Category> listCategory);
}
