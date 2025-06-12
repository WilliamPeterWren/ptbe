package com.tranxuanphong.productservice.service;

import com.tranxuanphong.productservice.dto.response.ProductResponse;
import com.tranxuanphong.productservice.entity.Product;
import com.tranxuanphong.productservice.exception.AppException;
import com.tranxuanphong.productservice.exception.ErrorCode;
import com.tranxuanphong.productservice.mapper.ProductMapper;
import com.tranxuanphong.productservice.repository.mongo.ProductRepository;

import org.springframework.stereotype.Service;
import org.springframework.security.access.prepost.PreAuthorize;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;



@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class ShipperProductService {

  ProductRepository productRepository;  
  ProductMapper productMapper;

  
  @PreAuthorize("hasAnyRole('ROLE_SHIPPER','ROLE_STAFF','ROLE_ADMIN')")
  public ProductResponse updateSoldById(String id, Long sold){
    Product product = productRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.PRODUCTID_INVALID));

    System.out.println("update sold: " + product.getSold() + sold);
    
    product.setSold(product.getSold() + sold);

    return productMapper.toProductResponse(productRepository.save(product));
  }

}
