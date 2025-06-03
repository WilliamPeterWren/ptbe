package com.tranxuanphong.productservice.service;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tranxuanphong.productservice.dto.request.CategoryCreateRequest;
import com.tranxuanphong.productservice.dto.request.CategoryUpdateRequest;
import com.tranxuanphong.productservice.dto.response.CategoryResponse;
import com.tranxuanphong.productservice.entity.Category;
import com.tranxuanphong.productservice.exception.AppException;
import com.tranxuanphong.productservice.exception.ErrorCode;
import com.tranxuanphong.productservice.mapper.CategoryMapper;
import com.tranxuanphong.productservice.repository.httpclient.PeterClient;
// import com.tranxuanphong.productservice.repository.elasticsearch.CategoryElasticsearchRepository;
import com.tranxuanphong.productservice.repository.httpclient.UserClient;
import com.tranxuanphong.productservice.repository.mongo.CategoryRepository;
import com.tranxuanphong.productservice.repository.mongo.ProductRepository;
import com.tranxuanphong.productservice.utils.GenerateSlug;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class CategoryService {
  CategoryRepository categoryRepository;
  ProductRepository productRepository;
  CategoryMapper categoryMapper;
  UserClient userClient;
  PeterClient peterClient;

  GenerateSlug generateSlug;
  // CategoryElasticsearchRepository categoryElasticsearchRepository;

  @PreAuthorize("hasRole('ROLE_SELLER')")
  public CategoryResponse create(CategoryCreateRequest request){
    String email = SecurityContextHolder.getContext().getAuthentication().getName(); 
    if(request.getCategoryName() == null || request.getCategoryName().isEmpty()){
      throw new AppException(ErrorCode.CATEGORYID_INVALID);
    }
    
    String sellerId = userClient.userId(email);


    List<Category> categories = categoryRepository.findBySellerId(sellerId);
    for(Category c: categories){
      if(c.getCategoryName().equals(request.getCategoryName())){
        throw new AppException(ErrorCode.CATEGORY_EXISTS);
      }
    }

    String slug = generateSlug.generateSlug(request.getCategoryName());

    Category category = categoryMapper.toCategory(request);
    category.setSellerId(sellerId);
    category.setSlug(slug);

    // categoryElasticsearchRepository.save(category);

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

  @PreAuthorize("hasRole('ROLE_SELLER')")
  public CategoryResponse update(String slug, CategoryUpdateRequest request){
    String email = SecurityContextHolder.getContext().getAuthentication().getName(); 
    String sellerId = userClient.userId(email);

    List<Category> categories = categoryRepository.findBySellerId(sellerId);
    for(Category c: categories){
      if(c.getCategoryName().equals(request.getCategoryName())){
        throw new AppException(ErrorCode.CATEGORY_EXISTS);
      }
    }

    Category category = categoryRepository.findBySlug(slug).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTS));
    categoryMapper.updateCategory(category, request);
    return categoryMapper.toCategoryResponse(category);
  }

  @PreAuthorize("hasRole('ROLE_SELLER')")
  public void delete(String id){
    String email = SecurityContextHolder.getContext().getAuthentication().getName(); 
    String sellerId = userClient.userId(email);

    List<Category> categories = categoryRepository.findBySellerId(sellerId);

    boolean checkCategoryInSeller = false;
    for(Category c:categories){
      if(c.getId().equals(id)){
        checkCategoryInSeller = true;
        break;
      }
    }

    if(!checkCategoryInSeller){
      throw new AppException(ErrorCode.CATEGORY_SELLER_NOT_MATCH);
    }

    if(productRepository.existsByCategoryId(id)){
      throw new AppException(ErrorCode.PRODUCT_IN_CATEGORY_EXIST);
    }

    categoryRepository.deleteById(id);
  }

  // public List<Category> searchCategoriesByCriteria(String name, String sellerId) {
  //   return categoryElasticsearchRepository.searchByCriteria(name, sellerId);
  // }
}
