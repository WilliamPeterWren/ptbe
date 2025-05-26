package com.tranxuanphong.productservice.mapper;


import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.tranxuanphong.productservice.dto.request.CategoryCreateRequest;
import com.tranxuanphong.productservice.dto.request.CategoryUpdateRequest;
import com.tranxuanphong.productservice.dto.response.CategoryResponse;
import com.tranxuanphong.productservice.entity.Category;


@Mapper(componentModel = "spring") 
public interface CategoryMapper {

  Category toCategory(CategoryCreateRequest request); 
  void updateCategory(@MappingTarget Category category, CategoryUpdateRequest request); 
  CategoryResponse toCategoryResponse(Category category); 
  List<CategoryResponse> toListCategoryResponse(List<Category> listCategory);
}
