package com.tranxuanphong.productservice.service;

import org.springframework.stereotype.Service;

import com.tranxuanphong.productservice.dto.request.CreateProductRequest;
import com.tranxuanphong.productservice.dto.request.UpdateProductRequest;
import com.tranxuanphong.productservice.dto.response.ProductResponse;
import com.tranxuanphong.productservice.entity.Product;
import com.tranxuanphong.productservice.exception.AppException;
import com.tranxuanphong.productservice.exception.ErrorCode;
import com.tranxuanphong.productservice.mapper.ProductMapper;
import com.tranxuanphong.productservice.repository.CategoryRepository;
import com.tranxuanphong.productservice.repository.ProductRepository;
import com.tranxuanphong.productservice.repository.httpclient.UserClient;
import com.tranxuanphong.productservice.utils.GenerateSlug;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Slf4j
public class ProductService {
  ProductRepository productRepository;
  CategoryRepository categoryRepository;
  ProductMapper productMapper;
  UserClient userClient;

  GenerateSlug generateSlug;

  ModelMapper modelMapper;

  public ProductResponse create(CreateProductRequest request){

    // System.out.println(request.toString());

    
    if(!userClient.existsId(request.getSellerId())){
      throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    if(!categoryRepository.existsById(request.getCategoryId())){
      throw new AppException(ErrorCode.CATEGORY_NOT_EXISTS);
    }

    String slug = generateSlug.generateSlug(request.getProductName());

    request.setSlug(slug);

    Product product = productMapper.toProduct(request);

    return productMapper.toProductResponse(productRepository.save(product));
  }

  public List<ProductResponse> getAll(){
    return productMapper.toListProductResponse(productRepository.findAll());
  }

  public Page<ProductResponse> getPaginatedProducts(int page, int size) {    
    Page<Product> list = productRepository.findAll(PageRequest.of(page, size));
        return list.map(product -> modelMapper.map(product, ProductResponse.class));
  }


  public ProductResponse getOne(String slug){
    Product product = productRepository.findBySlug(slug).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTS));
    return productMapper.toProductResponse(product);
  }

  public ProductResponse update(String slug, UpdateProductRequest request){
    
    if(!userClient.existsId(request.getSellerId())){
      throw new AppException(ErrorCode.UNAUTHENTICATED);
    }

    Product product = productRepository.findBySlug(slug).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTS));
    
    if(productRepository.existsByProductName(request.getProductName())){
      throw new AppException(ErrorCode.PRODUCT_EXISTS);
    }

    productMapper.updateProduct(product, request);
    
    return productMapper.toProductResponse(product);
  }

  public void delete(String id){
    productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTS));

    productRepository.deleteById(id);
  }
}
